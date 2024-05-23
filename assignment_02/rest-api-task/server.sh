#!/bin/bash

# Function to handle cleanup
cleanup() {
    echo "Stopping server..."
    kill $server_pid
    echo "Done."
    exit 0
}

# Trap the interrupt signal (Ctrl + C) to execute the cleanup function
trap cleanup INT

# Run the server
echo "Starting server..."
./venv/bin/python -m uvicorn server.server:app &

sleep 2

# Wait for the server to finish
wait $server_pid
