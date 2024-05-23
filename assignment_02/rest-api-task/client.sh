#!/bin/bash

# Run the client
echo "Starting client..."
./venv/bin/python -m uvicorn client.client:app --port 12345