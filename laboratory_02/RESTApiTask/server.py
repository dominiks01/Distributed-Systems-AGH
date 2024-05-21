from fastapi import FastAPI, Request, Form
from dotenv import load_dotenv, dotenv_values
from pathlib import Path
import requests, os
import json
import secrets
import valid_voivodeships
import multiprocessing, threading, concurrent.futures
from typing import Optional
import pandas as pd
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
import csv
import copy
from fastapi.security.api_key import APIKeyHeader
from fastapi import Security, HTTPException, Depends
from enum import Enum
from starlette.status import *
from fastapi.security import OAuth2PasswordBearer, APIKeyQuery

app = FastAPI()

valid_voivodeships = valid_voivodeships.Voivodeships.list()
config = dotenv_values(".env")
api_key = config["Geocode-Api"]

record_api = f"""https://api.zabytek.gov.pl/nidrestapi/api/data/geoportal/otwarteDaneZestawienieZen"""
geo_api = f'https://api.opencagedata.com/geocode/v1/json'

API_KEYS = config["My-API"]
LIMIT_DATA = 10
api_key_query = APIKeyQuery(name="api_key")

def get_api_key(api_key_header: str = Security(api_key_query)) -> str:
    if api_key_header in API_KEYS:
        return api_key_header

    raise HTTPException(
        status_code=HTTP_401_UNAUTHORIZED,
        detail="Invalid or missing API Key",
    )

def filter_data(
    data: dict,
    town: Optional[str] = None, 
    community: Optional[str] = None, 
    county: Optional[str] = None, 
    voivodeship: Optional[str] = None
) -> dict:
    df = pd.json_normalize(data)

    if validate_data(town, TerritorialUnit.town):
        df = df[df["miejscowosc"] == town]

    if validate_data(community, TerritorialUnit.community): 
        df = df[df["gmina"] == community]

    if validate_data(county, TerritorialUnit.county):
        df = df[df["powiat"] == county]

    if validate_data(voivodeship, TerritorialUnit.voivodeship):
        df = df[df["wojewodztwo"] == voivodeship]

    return df.to_dict(orient='records')


@app.get("/record/categories/{category}")
async def get_records(
    category: str,
    api_key: str = Security(get_api_key),
    county: Optional[str] = None, 
    community: Optional[str] = None, 
    town: Optional[str] = None, 
    voivodeship: Optional[str] = None, 
    limit: int = LIMIT_DATA
):
    try:
        url = record_api + f"?format=json&filter=['funkcja','=','{category.lower()}']&take={LIMIT_DATA}"
        data = requests.get(url).json()["data"]
    except requests.exceptions.RequestException as e: 
        raise HTTPException(
            status_code=HTTP_501_NOT_IMPLEMENTED,
            detail="Serwer natrafił na krytyczny błąd przy odpytywaniu serwisów!",
        )

    return filter_data(data, town=town, community=community, county=county, voivodeship=voivodeship)

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
    pins_for_map = [ x for x in data if x["geolocation"] == "false" ]

    return list_with_pins + pins_for_map


@app.get("/record/categories/")
async def get_categories(
    api_key: str = Security(get_api_key), 
    limit: int = LIMIT_DATA
):
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = record_api + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        data = requests.get(url).json()["data"]
        
        for elem in {* [(x["funkcja"]).capitalize() for x in data] }:
            results.add(elem)
            if len(results) >= limit:
                break
 
    return  [ {"funkcja" : x} for x in results ]


@app.get("/record/communities/")
async def get_communities(
    api_key: str = Security(get_api_key),
    limit: int = LIMIT_DATA
): 
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = record_api + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        data = requests.get(url).json()["data"]
        
        for elem in {* [(x["gmina"]).capitalize() for x in data] }:
            results.add(elem)
            if len(results) >= limit:
                break
 
    return  [ {"gmina" : x} for x in results ]


@app.get("/record/counties/")
async def get_counties(
    api_key: str = Security(get_api_key),
    limit: int = LIMIT_DATA
): 
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = record_api + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        data = requests.get(url).json()["data"]
        
        for elem in {* [(x["powiat"]).capitalize() for x in data] }:
            results.add(elem)
            if len(results) >= limit:
                break
 
    return  [ {"powiat" : x} for x in results ]


class TerritorialUnit(str, Enum):
    voivodeship = "voivodeship"
    county = "county"
    community = "community"
    town = "town"


def validate_data(
    name: str, 
    type: TerritorialUnit 
): 
    if name == "None" or name == "" or name == None:
        return False

    voivodeships = copy.copy(valid_voivodeships)

    match type:
        case TerritorialUnit.voivodeship :
            if name in voivodeships:
                return True 
        
        case TerritorialUnit.county: 
            return method_line_by_line("./database/wykaz_jednosetk_terytorialnych.csv", name, 4)

        case TerritorialUnit.community:
            return method_line_by_line("./database/wykaz_jednosetk_terytorialnych.csv", name, 4)

        case TerritorialUnit.town:
            return method_line_by_line("./database/wykaz_miejscowosci.csv", name, 6)

        case _:
            return False


@app.get("/record/")
async def get_record(
    api_key: str = Security(get_api_key),
    voivodeship: Optional[str] = None, 
    county: Optional[str] = None, 
    community: Optional[str] = None, 
    town: Optional[str] = None, 
    limit: int = LIMIT_DATA
): 
    search_strategy = 0
    print(voivodeship, type(voivodeship))

    if (voivodeship != "None" and voivodeship != "" and voivodeship != None) and validate_data(voivodeship, TerritorialUnit.voivodeship):
        search_strategy =  1 
    elif voivodeship != "None" and voivodeship != "" and voivodeship != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa województwa",
        )
    
    if validate_data(county, TerritorialUnit.county):
        search_strategy = 2
    elif county != "None" and county != ""  and county != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa powiatu",
        )

    if validate_data(community, TerritorialUnit.community):
        search_strategy =  3 
    elif community != "None" and community != ""  and community != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa gminy",
        )

    if validate_data(town, TerritorialUnit.town):
        search_strategy = 4
    elif town != "None" and town != ""  and town != None:
        raise HTTPException(
            status_code=HTTP_404_NOT_FOUND,
            detail="Zła nazwa miejscowości",
        )

    url = record_api

    match search_strategy:
        case 4:
            url += f"?format=json&filter=['miejscowosc','=','{town.title()}']"
        case 3: 
            url += f"?format=json&filter=['gmina','=','{community.title()}']"
        case 2:
            url += f"?format=json&filter=['powiat','=','{county.lower()}']"
        case 1: 
            url += f"?format=json&filter=['wojewodztwo','=','{voivodeship.lower()}']"
        case 0:
            url += f"?format=json&take={limit}"
    
    data = requests.get(url).json()["data"]
    df = filter_data(data, town=town, community=community, county=county, voivodeship=voivodeship)[:LIMIT_DATA]

    return process_data(df)[:limit]


@app.get("/record/towns/")
async def get_towns(
    api_key: str = Security(get_api_key),
    limit: int = LIMIT_DATA
): 
    voivodeships = copy.copy(valid_voivodeships)
    results = set()

    while len(results) < limit:
        url = record_api + f"?format=json&filter=['wojewodztwo','=','{voivodeships.pop()}']&take={LIMIT_DATA*10}"
        data = requests.get(url).json()["data"]
        
        for elem in {* [(x["miejscowosc"]).capitalize() for x in data] }:
            results.add(elem)
            if len(results) >= limit:
                break
 
    return  [ {"miejscowość" : x} for x in results ]


def method_line_by_line(filename, target: str, column: int) -> dict:
    find = False
    with open(filename, 'r', encoding='utf_8_sig') as csvfile:
        reader = csv.reader(csvfile, delimiter=';')

        try:
            for row in reader:
                if row[column] == target:
                    find = True
                    break

        except Exception as e:
            pass
    
    return find


def worker(procnum: int, item: dict, return_dict: dict) -> None:
    try:
        params = {
            'q': f"""{item['nrAdresowy']}%20{item["ulica"]}%20{item["miejscowosc"]}%20{item["gmina"]}""",
            'key': api_key
        }

        response = requests.get(geo_api, params=params)
        print(response)

        if response.status_code == 200:
            response = response.json()['results']

            lat = response[0]['geometry']['lat']
            lng = response[0]['geometry']['lng']

            response[0]['geometry'].pop('lat')
            response[0]['geometry'].pop('lng')

            item["lat"] = str(lat)
            item["lng"] = str(lng)
            item["formatted"] = response[0]["formatted"]
            response[0].pop('formatted')

            item.update(response[0])

        else:
            item["lat"] = ""
            item["lng"] = ""
            item["formatted"] = ""
            item["annotations"] = ""
            item["components"] = ""
            item["bounds"] = ""

        return_dict[procnum] = item


    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=HTTP_501_NOT_IMPLEMENTED,
            detail="Serwer natrafił na krytyczny błąd przy odpytywaniu serwisów!",
        )


