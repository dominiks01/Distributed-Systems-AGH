#!/bin/bash

if [ $# -lt 2 ]; then
  echo "Usage: $0 <technician_name> <service1> [<service2>]"
  exit 1
fi

TECHNICIAN_NAME=$1
shift
SERVICES=$@

mvn exec:java -Dexec.mainClass="org.example.technician.Technician" -Dexec.args="$TECHNICIAN_NAME $SERVICES"
