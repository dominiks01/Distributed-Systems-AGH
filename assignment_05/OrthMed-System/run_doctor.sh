#!/bin/bash

if [ $# -lt 1 ]; then
  echo "Usage: $0 <doctor_name>"
  exit 1
fi

DOCTOR_NAME=$1

mvn exec:java -Dexec.mainClass="org.example.doctor.Doctor" -Dexec.args="$DOCTOR_NAME"
