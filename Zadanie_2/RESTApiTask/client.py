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
import copy

app = FastAPI()

valid_voivodeships = valid_voivodeships.Voivodeships.list()
config = dotenv_values(".env")
api_key = config["My-API"]


app.mount("/static", StaticFiles(directory="./static", html=True), name="static")
templates = Jinja2Templates(directory="templates")

@app.get("/")
async def index(request: Request):
    return templates.TemplateResponse(
        request, "index.html",
        {"data" : valid_voivodeships})


@app.post("/query_example/")
async def query_example(
    request: Request, 
    voivodeship: Optional[str] = Form(None), 
    county: Optional[str] = Form(None), 
    community: Optional[str] = Form(None), 
    town: Optional[str] = Form(None)
) :
    url = f"http://localhost:8000/record?api_key={api_key}&town={town}&community={community}&county={county}&voivodeship={voivodeship}"
    data = requests.get(url)

    if data.status_code != 200:

        return templates.TemplateResponse(
            request, "error.html",
            {"data": data.json()}
    )

    return templates.TemplateResponse(
        request, "answer.html",
        {"data": data.json()}
    )