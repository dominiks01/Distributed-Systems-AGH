# OrthMed Service System
# Overview

The OrthMed Service System is a messaging-based application designed to streamline communication between doctors and technicians in a medical environment.

# Features

* Doctors can request services for their clients.
* Technicians receive and process these service requests.
* Processed results are sent back to specific doctors.
* An admin interface for managing and monitoring the system.

# Components
Doctor:
* Sends service requests.
* Receives processed results from technicians.

Technician:
* Processes incoming service requests.
* Sends back results to the requesting doctor.

Common:
*  Shared configurations and utilities.
Service:
* Enum defining the types of services available.

Prerequisites

    Java 11 or higher
    RabbitMQ Server

Setup and Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd <repository-directory>
```
2. Build the project:
Ensure you have Maven installed. Then run:
```bash
mvn clean install
```
3. Run RabbitMQ Server:
Make sure RabbitMQ is running on your machine.

# Running the Application
To run application the running scripts were provided. 
```bash
./run_administrator.sh <administrator_name> 
./run_doctor.sh <doctor_name> 
./run_technician.sh <technician_name> <services_supported> 
```

# Usage
Doctor Commands

    <serviceName> <clientName> - Request a service for a client.
    quit - Exit the application.
    help - List available services.

Technician Commands

    quit - Exit the application.
    Automatically processes incoming service requests and sends the results back to the requesting doctor.

Administrator Commands

    info <message> - Send administrative message to all clients.
    help - List available commands.
