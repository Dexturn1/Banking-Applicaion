# Banking App

Demo project for Spring Boot Banking app.

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
