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
Spring Boot with webflux, running on netty, and accessing the database with reactive repositories. 
I noticed the exercise is driving to a reactive approach. So that is why I used Spring Boot Web Flux with Reactive JPA  

Database:
I think it would be nice to see the responses of the poller over time, so I saved all attempts in Elasticsearch.
It is possible, if needed, to query the results of the poller from the previous days for a specific service. 

Docker Compose: 
Only for test propose. It will spin up an elasticsearch and kibana dashboard containers.
I thought it would be nice to have a way to see the data collected in a dashboard. Not asked! but an extra ;)  

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

####See the data in Kibana!
* Go to your favorite browser and type the url http://localhost:5601
* Find the option 'Dev Tools' on the left menu
* Type the following command in the console: 

```
 GET service-result-latest/_search
```

####I want to use API's!

No problem: Access the [swagger-ui](http://localhost:8080/swagger-ui/) link to see all the available API's