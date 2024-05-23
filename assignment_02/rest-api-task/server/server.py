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
import configparser
import logging
from .services import *
from common import utils, valid_voivodeships

logging.basicConfig(format="%(asctime)s %(message)s", level=logging.INFO)
app = FastAPI()

# read config
config = configparser.ConfigParser()
config.read("config.ini")
LIMIT_DATA = int(config.get("Query Settings", "LIMIT_DATA"))

# Load environment variables
config = dotenv_values(".env")

# Load valid voivodeships list
valid_voivodeships = valid_voivodeships.Voivodeships.list()

record_api = f"""https://api.zabytek.gov.pl/nidrestapi/api/data/geoportal/otwarteDaneZestawienieZen"""

API_KEYS = config["My-API"]
api_key_query = APIKeyQuery(name="api_key")


def get_api_key(api_key_header: str = Security(api_key_query)):
    """Retrieve API key from header

    Args:
        api_key_header (str, optional): header. Defaults to Security(api_key_query).

    Raises:
        HTTPException: HTTP_401_UNAUTHORIZED if key is invalid

    Returns:
        str: header
    """
    if api_key_header in API_KEYS:
        return api_key_header

    raise HTTPException(
        status_code=HTTP_401_UNAUTHORIZED,
        detail="Invalid or missing API Key",
    )


@app.get("/record/categories/{category}")
async def get_records(
    category: str,
    api_key: str = Security(get_api_key),
    county: Optional[str] = None,
    community: Optional[str] = None,
    town: Optional[str] = None,
    voivodeship: Optional[str] = None,
    limit: int = LIMIT_DATA,
):
    """Get entities from selected category

    Args:
        category (str): category label. Obligatory
        api_key (str, optional): API key. Defaults to Security(get_api_key).
        county (Optional[str], optional): County label. Defaults to None.
        community (Optional[str], optional): Community label. Defaults to None.
        town (Optional[str], optional): Town label. Defaults to None.
        voivodeship (Optional[str], optional): Voivodeship label. Defaults to None.
        limit (int, optional): Restriction for no. entities. Defaults to LIMIT_DATA.

    Raises:
        HTTPException: Error if server could not handle request

    Returns:
        dict: filtered entities
    """

    query_params = {
        "format": "json",
        "filter": f"['funkcja','=','{category.lower()}']",
        "take": LIMIT_DATA,
    }

    url = f"{record_api}?{urlencode(query_params)}"

    try:
        response = requests.get(url)
        response.raise_for_status()  # raise HTTPError if occcured
        data = response.json()["data"]
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=HTTP_501_NOT_IMPLEMENTED,
            detail="Serwer natrafił na krytyczny błąd przy odpytywaniu serwisów!",
        )
    except Exception:
        raise HTTPException(
            status_code=HTTP_500_INTERNAL_SERVER_ERROR,
            detail="HTTP_500_INTERNAL_SERVER_ERROR",
        )

    return filter_data(
        data, town=town, community=community, county=county, voivodeship=voivodeship
    )


@app.get("/record/categories/")
async def get_categories(api_key: str = Security(get_api_key), limit: int = LIMIT_DATA):
    """list all categories

    Args:
        api_key (str, optional): Defaults to Security(get_api_key).
        limit (int, optional):  Defaults to LIMIT_DATA.

    Returns:
        dict: categories
    """
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = (
            record_api
            + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        )
        data = requests.get(url).json()["data"]

        for elem in {*[(x["funkcja"]).capitalize() for x in data]}:
            results.add(elem)
            if len(results) >= limit:
                break

    return [{"funkcja": x} for x in results]


@app.get("/record/communities/")
async def get_communities(
    api_key: str = Security(get_api_key), limit: int = LIMIT_DATA
):
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = (
            record_api
            + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        )
        data = requests.get(url).json()["data"]

        for elem in {*[(x["gmina"]).capitalize() for x in data]}:
            results.add(elem)
            if len(results) >= limit:
                break

    return [{"gmina": x} for x in results]


@app.get("/record/counties/")
async def get_counties(api_key: str = Security(get_api_key), limit: int = LIMIT_DATA):
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = (
            record_api
            + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        )
        data = requests.get(url).json()["data"]

        for elem in {*[(x["powiat"]).capitalize() for x in data]}:
            results.add(elem)
            if len(results) >= limit:
                break

    return [{"powiat": x} for x in results]


@app.get("/record")
async def get_record(
    api_key: str = Security(get_api_key),
    voivodeship: Optional[str] = None,
    county: Optional[str] = None,
    community: Optional[str] = None,
    town: Optional[str] = None,
    limit: int = LIMIT_DATA,
):
    search_strategy = 0

    if (
        voivodeship != "None" and voivodeship != "" and voivodeship != None
    ) and validate_data(voivodeship, TerritorialUnit.voivodeship):
        search_strategy = 1
    elif voivodeship != "None" and voivodeship != "" and voivodeship != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa województwa",
        )

    if validate_data(county, TerritorialUnit.county):
        search_strategy = 2
    elif county != "None" and county != "" and county != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa powiatu",
        )

    if validate_data(community, TerritorialUnit.community):
        search_strategy = 3
    elif community != "None" and community != "" and community != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa gminy",
        )

    if validate_data(town, TerritorialUnit.town):
        search_strategy = 4
    elif town != "None" and town != "" and town != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa miejscowości",
        )

    url = record_api

    search_strategy_to_param = {
        4: {"key": "miejscowosc", "value": town.title()},
        3: {"key": "gmina", "value": community.title()},
        2: {"key": "powiat", "value": county.lower()},
        1: {"key": "wojewodztwo", "value": voivodeship.lower()},
        0: {"key": "take", "value": limit * 20},
    }

    query_params = {"format": "json"}

    for strategy, param in search_strategy_to_param.items():
        if strategy > 0:
            query_params["filter"] = f"['{param['key']}','=','{param['value']}']"
        else:
            query_params["take"] = param["value"]

    url += "?" + urlencode(query_params)
    data = requests.get(url).json()["data"]

    df = filter_data(
        data, town=town, community=community, county=county, voivodeship=voivodeship
    )[:LIMIT_DATA]

    logging.info(f"Query executed, found: {len(df)} entries!")

    return process_data(df)[:limit]


@app.get("/record/towns/")
async def get_towns(api_key: str = Security(get_api_key), limit: int = LIMIT_DATA):
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = (
            record_api
            + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        )
        data = requests.get(url).json()["data"]

        for elem in {*[(x["miejscowosc"]).capitalize() for x in data]}:
            results.add(elem)
            if len(results) >= limit:
                break

    return [{"miejscowość": x} for x in results]
