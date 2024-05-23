import os
import json
import secrets
import csv
import copy
import multiprocessing
import threading
import concurrent.futures

from pathlib import Path
from typing import Optional
from enum import Enum

import requests
import pandas as pd
from dotenv import load_dotenv, dotenv_values

from fastapi import FastAPI, Request, Form, Security, HTTPException, Depends
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from fastapi.security.api_key import APIKeyHeader, APIKeyQuery
from fastapi.security import OAuth2PasswordBearer
from starlette.status import *
from urllib.parse import urlencode, urlunparse
from .utils import *
import configparser
from common import valid_voivodeships
import logging

valid_voivodeships = valid_voivodeships.Voivodeships.list()

config = dotenv_values(".env")
api_key = config["Geocode-Api"]
geo_api = f"https://api.opencagedata.com/geocode/v1/json"


def process_data(data):
    manager = multiprocessing.Manager()
    return_dict = manager.dict()
    jobs = []

    for id, item in enumerate(data):
        if item["nrAdresowy"]:
            item["geolocation"] = "true"
            p = multiprocessing.Process(
                target=process_item, args=(id, item, return_dict)
            )
            jobs.append(p)
            p.start()
        else:
            item.update(
                {
                    "geolocation": "false",
                    "lat": "",
                    "lng": "",
                    "formatted": "",
                    "annotations": "",
                    "components": "",
                    "bounds": "",
                }
            )

    for proc in jobs:
        proc.join()

    list_with_pins = list(return_dict.values())
    pins_for_map = [x for x in data if x["geolocation"] == "false"]

    return list_with_pins + pins_for_map


def process_item(procnum, item, return_dict):
    try:
        if item["nrAdresowy"]:
            params = {
                "q": f"{item['nrAdresowy']} {item['ulica']} {item['miejscowosc']} {item['gmina']}",
                "key": api_key,
            }

            response = requests.get(geo_api, params=params)

            if response.status_code == 200:
                response = response.json()["results"]

                lat = response[0]["geometry"]["lat"]
                lng = response[0]["geometry"]["lng"]

                response[0]["geometry"].pop("lat")
                response[0]["geometry"].pop("lng")

                item.update(
                    {
                        "lat": str(lat),
                        "lng": str(lng),
                        "formatted": response[0]["formatted"],
                        "annotations": response[0]["annotations"],
                        "components": response[0]["components"],
                        "bounds": response[0]["bounds"],
                    }
                )

            else:
                logging.error(response)
                item.update(
                    {
                        "lat": "",
                        "lng": "",
                        "formatted": "",
                        "annotations": "",
                        "components": "",
                        "bounds": "",
                    }
                )

            return_dict[procnum] = item

    except requests.exceptions.RequestException as e:
        logging.error(e)
        raise HTTPException(
            status_code=501,
            detail="Server encountered a critical error while querying services!",
        )


def method_line_by_line(filename, target: str, column: int) -> dict:
    find = False
    with open(filename, "r", encoding="utf_8_sig") as csvfile:
        reader = csv.reader(csvfile, delimiter=";")

        try:
            for row in reader:
                if row[column] == target:
                    find = True
                    break

        except Exception as e:
            pass

    return find


def filter_data(
    data: dict,
    town: Optional[str] = None,
    community: Optional[str] = None,
    county: Optional[str] = None,
    voivodeship: Optional[str] = None,
):
    """Data filtering service.
    Get entities from selected terytorial units.
    If unit is none then ignore filtering

    Args:
        data (dict): full data
        town (Optional[str], optional): Town label. Defaults to None.
        community (Optional[str], optional): Community label. Defaults to None.
        county (Optional[str], optional): Count label. Defaults to None.
        voivodeship (Optional[str], optional): Voivodeship label. Defaults to None.

    Returns:
        dict: Filtered data
    """
    df = pd.json_normalize(data)

    filters = {
        "miejscowosc": (town, TerritorialUnit.town),
        "gmina": (community, TerritorialUnit.community),
        "powiat": (county, TerritorialUnit.county),
        "wojewodztwo": (voivodeship, TerritorialUnit.voivodeship),
    }

    for column, (value, unit_type) in filters.items():
        if value is not None and value != "None" and validate_data(value, unit_type):
            df = df[df[column] == value]

    return df.to_dict(orient="records")


def validate_data(value: Optional[str], unit_type: TerritorialUnit):
    """Data validating function.
        - Voivodeship is valid if it exists in vaild voivodeships
        - For rest units program check is there is entity in database
          with given name.
    Args:
        value (Optional[str]): Terrytorial unit label
        unit_type (str): Terrytorial unit type

    Returns:
        bool: true if valid, false otherwise
    """
    if value is None or value == "None" or value == "":
        return False

    voivodeships = copy.copy(valid_voivodeships)

    match unit_type:
        case TerritorialUnit.voivodeship:
            if value in voivodeships:
                return True

        case TerritorialUnit.county:
            return method_line_by_line(
                "./database/wykaz_jednosetk_terytorialnych.csv",
                value,
                TerrytorialUnitIndex.COUNTY,
            )

        case TerritorialUnit.community:
            return method_line_by_line(
                "./database/wykaz_jednosetk_terytorialnych.csv",
                value,
                TerrytorialUnitIndex.COMMUNITY,
            )

        case TerritorialUnit.town:
            return method_line_by_line(
                "./database/wykaz_miejscowosci.csv", value, TerrytorialUnitIndex.TOWN
            )

        case _:
            return False
