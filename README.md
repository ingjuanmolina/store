# Purchase order service

## Description
This is a simple example about how to manage a purchase order. It was built using:
- Spring Boot v2.5.4
- Java 11
- JpaRepositories
- In memory database H2 1.4.200

## Run
This is a Maven project. By default, the app runs on port 8080. To run it on a different port just add `server.port=8081` (changing port number) on **_src/main/resources/application.properties_** file.

At startup, **_src/main/resources/data.sql_** file creates a couple of products, Apple and Orange. More products could be added in the same way. 

## Endpoints
- Get all purchase orders: **GET** http://localhost:8080/v1/purchase-orders
- Create purchase order: **POST** http://localhost:8080/v1/purchase-orders

## Postman tests
A Postman collection has been added with some basic tests. You can import it to Postman following [these instructions](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman)
