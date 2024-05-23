from fastapi import FastAPI, Request, Form, Security, HTTPException, Depends
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from fastapi.security.api_key import APIKeyHeader, APIKeyQuery
from fastapi.security import OAuth2PasswordBearer
from starlette.status import *

from dotenv import load_dotenv, dotenv_values
from pathlib import Path
from typing import Optional
from enum import Enum
from urllib.parse import urlencode, urlunparse

import requests
import os
import json
import secrets
import pandas as pd
import csv
import copy
from common import valid_voivodeships
import configparser

import multiprocessing
import threading
import concurrent.futures

from urllib.parse import urlencode, urlunparse

app = FastAPI()

# Load environment variables
config = dotenv_values(".env")
api_key = config["My-API"]

# Get list ov valid voivodeships
valid_voivodeships = valid_voivodeships.Voivodeships.list()

# Mount static file directory
app.mount("/static", StaticFiles(directory="./static", html=True), name="static")

# Set up Jinja templates directory
templates = Jinja2Templates(directory="templates")


@app.get("/", response_class=HTMLResponse)
async def index(request: Request) -> Jinja2Templates.TemplateResponse:
    """Main page request handler

    Args:
        request (Request): interface class

    Returns:
        Jinja2Template.TemplateResponse: servis main page template
    """

    context = {"request": request, "data": valid_voivodeships}
    return templates.TemplateResponse("index.html", context)


@app.post("/query_example/")
async def query_example(
    request: Request,
    voivodeship: Optional[str] = Form(None),
    county: Optional[str] = Form(None),
    community: Optional[str] = Form(None),
    town: Optional[str] = Form(None),
) -> Jinja2Templates.TemplateResponse:
    """Testing example

    Args:
        request (Request):
        voivodeship (Optional[str], optional): voivodeship label. Defaults to Form(None).
        county (Optional[str], optional): county label. Defaults to Form(None).
        community (Optional[str], optional): community label. Defaults to Form(None).
        town (Optional[str], optional): town label. Defaults to Form(None).

    Returns:
        Jinja2Template.TemplateResponse: response template
    """

    query_params = {
        "api_key": api_key,
        "town": town,
        "community": community,
        "county": county,
        "voivodeship": voivodeship,
    }

    url = f"http://localhost:8000/record?{urlencode(query_params)}"

    try:
        response = requests.get(url)
        response.raise_for_status()  # Raise HTTPError for bad responses

    except requests.RequestException as e:
        return templates.TemplateResponse(
            "error.html", {"error_message": e, "request": request}
        )

    return templates.TemplateResponse(
        "answer.html", {"data": response.json(), "request": request}
    )
