# Heritage Registration System

A web service that implements functionalities based on open services providing REST APIs. The used services include:

- [National Heritage Register Dataset](https://dane.gov.pl/en/dataset/1130,rejestr-zabytkow-nieruchomych/resource/35929/table)
- [OpenCage Geocoding API](https://opencagedata.com/d)

## Overview

The purpose of the service is to retrieve heritage sites from the registry based on query parameters such as locality and district. The data will be processed to include additional information such as geolocation, which can then be displayed on the client side in the form of a simple map.

<img src="images/demo.png">

## Querying

Queries can be executed through a simple form.

## Usage

1. Navigate to the main folder:
```shell
cd rest-api-task
```

2. Create a Python virtual environment:
```shell
python -m venv myenv
source myenv/bin/activate
```

3. Activate the virtual environment:
```shell
source myenv/bin/activate
```

4. Install the required libraries:
``` shell
pip install -r requirements.txt
```

# API Keys
To ensure the proper functioning of the program, you will need a key for geolocation. The project uses the OpenCage Geocoding API. Simply replace the placeholder key in the `.env` file

```
Geocode-Api="YOUR KEY"
My-API=95a5843d98fdef6b1ea4e63b61b02b14
```

# Running the Application 
To start the server, simply execute the script:
```shell
./server.sh
```

Similarly, to start the client, run:
```shell
./client.sh
```
