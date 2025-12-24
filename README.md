# Banking App

This is a demo project for a Spring Boot Banking application, created for educational purposes.
It demonstrates how to build a RESTful API using Spring Boot, Spring Data JPA, and MySQL.

The project focuses on backend development concepts, including:
*   **REST API Development**: Creating endpoints for banking operations.
*   **Database Integration**: Using Spring Data JPA and Hibernate to interact with a MySQL database.
*   **Entity Management**: Defining data models like `Account`.
*   **Exception Handling**: Implementing custom exceptions for error management.
*   **DTO Pattern**: Using Data Transfer Objects to separate internal entities from API responses.

## Technologies

*   Java 25
*   Spring Boot 3.5.10-SNAPSHOT
*   Spring Data JPA
*   Spring Web
*   MySQL
*   Lombok

## Prerequisites

*   Java 25 or higher
*   Maven
*   MySQL Database

## Setup

1.  Clone the repository.
2.  Configure the database connection in `src/main/resources/application.properties`.
3.  Build the project:
    ```bash
    ./mvnw clean install
    ```

## Running the Application

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

## Docker

To build the Docker image:

```bash
docker build -t banking-app .
```
