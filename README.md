# Kry - Health Check Service 

## Daniel Machado Vasconcelos 

### Basic requirements:

* A user needs to be able to add a new service with URL and a name
* Added services have to be kept when the server is restarted
* Display the name, url, creation time and status for each service
* Provide a README in english with instructions on how to run the application

Prerequisites
-------------
* Java JDK 11
* Docker and Docker Compose
* A little of Positivity! :) 
 
### Architectural Decisions

Framework:
Docker Compose:
Database:

## How to build?
Clone this repo into new project folder (e.g., `kry-service-poller`).

```bash
git clone https://github.com/DanielMachadoVasconcelos/kry-code-test.git
cd kry-code-test
```

Run gradle command to build and run the tests.
**Note:** (A docker container running MySQL database will start on your computer) 

```bash
./gradlew clean build
```

## How to use?

####Starting the Application
```bash
./gradlew bootRun 
```

####Check the service is running and health!  \O/ 
```bash
curl --request GET  --url http://localhost:8081/health 
```
Or
```bash
curl --request GET --url http://localhost:8081/ping 
```

