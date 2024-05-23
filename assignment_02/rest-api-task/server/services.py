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


def process_data(data: dict) -> dict:
    manager = multiprocessing.Manager()
    return_dict = manager.dict()
    jobs = []

    for id, item in enumerate(data):
        if item["nrAdresowy"] != "":
            item["geolocation"] = "true"
            p = multiprocessing.Process(target=worker, args=(id, item, return_dict))
            jobs.append(p)
            p.start()
        else:
            item["geolocation"] = "false"
            item["lat"] = ""
            item["lng"] = ""
            item["formatted"] = ""
            item["annotations"] = ""
            item["components"] = ""
            item["bounds"] = ""

    for proc in jobs:
        proc.join()

    list_with_pins = return_dict.values()
    pins_for_map = [x for x in data if x["geolocation"] == "false"]

    return list_with_pins + pins_for_map


def worker(procnum: int, item: dict, return_dict: dict) -> None:
    try:
        params = {
            "q": f"""{item['nrAdresowy']}%20{item["ulica"]}%20{item["miejscowosc"]}%20{item["gmina"]}""",
            "key": api_key,
        }

        response = requests.get(geo_api, params=params)

        if response.status_code == 200:
            response = response.json()["results"]

            lat = response[0]["geometry"]["lat"]
            lng = response[0]["geometry"]["lng"]

            response[0]["geometry"].pop("lat")
            response[0]["geometry"].pop("lng")

            item["lat"] = str(lat)
            item["lng"] = str(lng)
            item["formatted"] = response[0]["formatted"]
            response[0].pop("formatted")

            item.update(response[0])

        else:
            logging.error(response)
            item["lat"] = ""
            item["lng"] = ""
            item["formatted"] = ""
            item["annotations"] = ""
            item["components"] = ""
            item["bounds"] = ""

        return_dict[procnum] = item

    except requests.exceptions.RequestException as e:
        logging.error(e)
        raise HTTPException(
            status_code=HTTP_501_NOT_IMPLEMENTED,
            detail="Serwer natrafił na krytyczny błąd przy odpytywaniu serwisów!",
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
        "miejscowosc": (town.capitalize(), TerritorialUnit.town),
        "gmina": (community.lower(), TerritorialUnit.community),
        "powiat": (county.lower(), TerritorialUnit.county),
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

    match unit_type:
        case TerritorialUnit.voivodeship:
            if value in valid_voivodeships:
                return True

        case TerritorialUnit.county:
            return method_line_by_line(
                "./database/wykaz_jednosetk_terytorialnych.csv",
                value.lower(),
                TerrytorialUnitIndex.COUNTY,
            )

        case TerritorialUnit.community:
            return method_line_by_line(
                "./database/wykaz_jednosetk_terytorialnych.csv",
                value.lower(),
                TerrytorialUnitIndex.COMMUNITY,
            )

        case TerritorialUnit.town:
            value = value.capitalize()
            return method_line_by_line(
                "./database/wykaz_miejscowosci.csv", value, TerrytorialUnitIndex.TOWN
            )

        case _:
            return False
