# 
# Laboratorium 2, REST 
# 
# Prosty serwis wyszukujący obiekty wpisane do 
# krajowego rejestru zabytków nieruchomych, oraz ewidencji zabytków nieruchomych. 
# Serwis przekazuje dane obiektu takie jak adres/funkcja oraz umożliwia zlokalizowanie 
# obiektów z adresem na mapie
# 
# Użyte API:
# Rejestr zabytków nieruchomych: https://dane.gov.pl/en/dataset/1130,rejestr-zabytkow-nieruchomych/resource/35929/table
# Ewidencja zabytków nieruchomych: https://dane.gov.pl/en/dataset/2627,ewidencja-zabytkow-nieruchomych/resource/36021/table
# OpenCage Geocoding API: https://opencagedata.com/

from fastapi import FastAPI, Request, Form
from dotenv import load_dotenv, dotenv_values
from pathlib import Path
import requests, os
import json
import valid_voivodeships
import multiprocessing, threading, concurrent.futures
from typing import Optional
import pandas as pd
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
import csv

app = FastAPI()
app.mount("/static", StaticFiles(directory="./static", html=True), name="static")
templates = Jinja2Templates(directory="templates")

valid_voivodeships = valid_voivodeships.Voivodeships.list()
config = dotenv_values(".env")
api_key = config["Geocode-Api"]

register_api = f"""https://api.zabytek.gov.pl/nidrestapi/api/data/geoportal/otwarteDaneZestawienieZrn"""
record_api = f"""https://api.zabytek.gov.pl/nidrestapi/api/data/geoportal/otwarteDaneZestawienieZen"""
geo_api = f'https://api.opencagedata.com/geocode/v1/json'

@app.get("/")
async def index(request: Request):
    return templates.TemplateResponse(
        request, "index.html",
        {"data" : valid_voivodeships})


def method_line_by_line(filename, target: str, column: int) -> dict:
    """ 
    Metoda do sprawdzania poprawności nazw miejscowości/gmin/powiatów. 
    Sprawdzam czy dana nazwa pojawia się w rejestrze TERYT Głównego urzędu statystycznego
    https://eteryt.stat.gov.pl/eTeryt/rejestr_teryt/udostepnianie_danych/baza_teryt/uzytkownicy_indywidualni/pobieranie/pliki_pelne.aspx?contrast=default
    
    :param filename:csv ścieżka do pliku rejestru 
    :param target: sprawdzana nazwa
    :param column: kolumna w pliku do porównania 
    """
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

def create_error_template(message: str, code: int, request: Request):
    return templates.TemplateResponse(
        request, "error.html", {
            "code": code, 
            "message": message
        }
    )


def fetch_data(api_url: str, data: dict) -> json:
    """ 
    Pobieranie i obróbka danych 

    :param api_url: url zapytania
    :param data: dane do parametrów zapytania
    :Return json: zwraca wszystkie obiekty spełniające parametry zapytania  
    """
    search_strategy = 0;

    is_town_valid = False
    is_community_valid = False
    is_county_valid = False
    is_voivodeship_valid = False

    county, community, town, voivodeship = data.values()

    # Jeśli w nazwie występują znaki takie jak " ", pomijam przy wyborze najbardziej optymalnego zapytania
    if town != None and method_line_by_line("./database/wykaz_miejscowosci.csv", town, 6):
        search_strategy = 1 if " " not in town else search_strategy
        is_town_valid = True
    elif town != None:
        raise ValueError(f"Nieznana nazwa miejscowości {town}")

    if community != None and method_line_by_line("./database/wykaz_jednosetk_terytorialnych.csv", community, 4):
        search_strategy = 2 if " " not in community else search_strategy
        is_community_valid = True
    elif community != None:
        raise ValueError(f"Nieznana nazwa gminy {community}")
   
    if county != None and method_line_by_line("./database/wykaz_jednosetk_terytorialnych.csv", county, 4):
        search_strategy = 3 if " " not in county else search_strategy
        is_county_valid = True
    elif county != None:
        raise ValueError(f"Nieznana nazwa powiatu {county}")

    # Filtrowanie po wielu parametrach nie jest wytłumaczone w dokumentacji Otwartch Danych
    # Z tego też powodu parametry filtrowania ustawiam na możliwie najbardziej szczegółowe, 
    # a następnie dane ręcznie filtuję
    # https://dane.gov.pl/en/dataset/1130,rejestr-zabytkow-nieruchomych/resource/35929/table
    match search_strategy:
        case 1: 
            api_url += f"?filter=['miejscowosc','=','{town.capitalize()}']&format=json"
        case 2: 
            api_url += f"?filter=['gmina','=','{community.capitalize()}']&format=json"
        case 3: 
            api_url += f"?filter=['powiat','=','{county.lower()}']&format=json"            
        case _: 
            api_url += f"?filter=['wojewodztwo','=','{voivodeship}']&format=json"            

    try:
        response = requests.get(api_url).json()
    except requests.exceptions.RequestException as e:
        raise SystemExit(e)

    df = pd.json_normalize(response["data"])

    if is_town_valid and search_strategy != 1:
        df = df[df["miejscowosc"] == town]

    if is_community_valid and search_strategy != 2: 
        df = df[df["gmina"] == community]

    if is_county_valid and search_strategy != 3:
        df = df[df["powiat"] == county]

    if search_strategy != 0:
        df = df[df["wojewodztwo"] == voivodeship]

    return df.to_dict(orient='records')


def worker(procnum: int, item: dict, return_dict: dict) -> None:
    """
    Dla przyśpieszenia działania, zapytania api wykonuję używając multiprocesingu 
    
    :param procnum: identyfikator procesu 
    :param item: przedmiot zapytania
    :param return_dict: struktura przechowywująca wyniki zapytań
    """
    
    try:
        params = {
            'q': f"""{item['nrAdresowy']}%20{item["ulica"]}%20{item["miejscowosc"]}%20{item["gmina"]}""",
            'key': api_key
        }

        response = requests.get(geo_api, params=params)

        if response.status_code == 200:
            lat = response.json()['results'][0]['geometry']['lat']
            lng = response.json()['results'][0]['geometry']['lng']

            item["lat"] = lat
            item["lng"] = lng 

        return_dict[procnum] = item


    except requests.exceptions.RequestException as e:
        print(e)
        pass
        raise Exception(e)


@app.post("/query_example/")
async def query_example(
    request: Request, 
    voivodeship: Optional[str] = Form(None), 
    county: Optional[str] = Form(None), 
    community: Optional[str] = Form(None), 
    town: Optional[str] = Form(None), 
    pin_on_map: Optional[bool] = Form(None)
) :

    data = {
        "county" : county.lower() if isinstance(county, str) else None, 
        "community": community.capitalize() if isinstance(community, str) else None, 
        "town": town.capitalize() if isinstance(town, str) else None, 
        "voivodeship" : voivodeship
    }

    register, record, cords = None, None, None

    try:
        with concurrent.futures.ThreadPoolExecutor() as executor:
            future = executor.submit(fetch_data, register_api, data)
            register = future.result()

        with concurrent.futures.ThreadPoolExecutor() as executor:
            future = executor.submit(fetch_data, record_api, data)
            record = future.result()

    except ValueError as e: 
        return create_error_template(e, 404, request)
    
    except OSError as e: 
        return create_error_template("Server critical error", 500, request)

    except Exception as e:
        return create_error_template("The server has encountered a situation it does not know how to handle.", 500, request)

    pins_for_map = [ x for x in record if x["nrAdresowy"] != "" ]

    if pin_on_map:

        manager = multiprocessing.Manager()
        return_dict = manager.dict()
        jobs = []

        for id, item in enumerate(pins_for_map):    
            p = multiprocessing.Process(target=worker, args=(id, item, return_dict))
            jobs.append(p)
            p.start()

        for proc in jobs:
            proc.join()
        
        pins_for_map = return_dict.values()

    lat = 52.237049
    lng = 21.017532

    try:
        params = {
            'q': f"""{town if town != None else ""}%20{community if community != None else ""}%20{county if county != None else ""}%20{voivodeship}""",
            'key': api_key
        }

        response = requests.get(geo_api, params=params)
    
        if response.status_code == 200:
            lat = response.json()['results'][0]['geometry']['lat']
            lng = response.json()['results'][0]['geometry']['lng']

    except requests.exceptions.RequestException as e:
        pass 
    
    stats = {
        "names": data,
        "no_elements_register": len(register), 
        "no_elements_ewidence": len(record)
    }

    return templates.TemplateResponse(
        request, "answer.html", 
        {
            "rejestr": register, 
            "ewidencja": record, 
            "lat": lat,
            "lng": lng, 
            "pins_for_map": pins_for_map,  
            "zoom": 12, 
            "stats": stats
        }
    )
