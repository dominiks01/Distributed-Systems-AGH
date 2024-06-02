#!/bin/bash

if [ $# -lt 1 ]; then
  echo "Usage: $0 <administrator_name>"
  exit 1
fi

ADMINISTRATOR_NAME=$1

mvn exec:java -Dexec.mainClass="org.example.administrator.Administrator" -Dexec.args="$ADMINISTRATOR_NAME"
