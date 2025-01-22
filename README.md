# Receipt Cruncher

This is a web service built to respond to 
the challenge provided by Fetch Rewards
https://github.com/fetch-rewards/receipt-processor-challenge

## Overview
The server stub was generated [OpenAPI Generator](https://openapi-generator.tech) project 
to conform to the provided api.yml contract. 
The business logic, H2 database, and everything else was done by me.

You can view the api documentation in swagger-ui by pointing to
http://localhost:8080/swagger-ui.html


# How To Run with Docker

If you do not have Java 8 and Maven installed on your system,
you can run this app using Docker.

simply navigate to the root directory of this project, and
run these commands:

build the image:
`docker build -t receipt-app .`

run the container:
`docker run -d -p 8080:8080 --name receipt-app-container receipt-app`

Wait a few seconds for it to spin up. 
Then you can reach the container on port 8080 at http://localhost:8080
You can use my provided postman test collection in the /postman folder

to stop the container:
`docker stop receipt-app-container`

remove the container:
`docker rm receipt-app-container`

remove the image:
`docker rmi receipt-app`

