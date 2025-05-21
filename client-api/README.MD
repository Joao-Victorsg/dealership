# Client API

## Description
The **Client API** is a Java-based service built with the Spring Boot framework. It provides functionalities to manage client information, including CPF, name, address, email, and registration date. The application is packaged as a JAR file, runs in a Docker container, and is designed for deployment on AWS ECS using Fargate. A Swagger page is available for API documentation.

## Technologies Used
- **Java 21**
- **Spring Boot**
- **Maven**
- **Docker**
- **Terraform** for infrastructure as code
- **LocalStack** for running the application locally with AWS services
- **JUnit 5**, **Pitest** and **TestContainers** for testing

## Features
- Create, retrieve, update, and delete client records
- Health check endpoint
- API documentation via Swagger
- Dockerized for easy deployment
- Infrastructure as code with Terraform
- **Automatic address lookup using ViaCEP API integration**

## Installation

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker (for containerization)
- Localstack (for local AWS services)

## Build & Run

### Locally
To run the application locally with all the dealership environment, you should run the [deploy-all.sh](../localstack-dealership/deploy-all.sh) script. It will create all the necessary 
infra resources in localstack and run the application. For more details, check the main [readme](../Readme.md).

## Main Endpoints
The API exposes the following endpoints. Full documentation is available at the Swagger page: `/swagger-ui.html`.

### **GET /v1/dealership/actuator/health**
Check the application's health status.

### **POST /v1/dealership/clients**
Create a new client.

**Example Payload:**
```json
{
  "name": "Joao",
  "cpf": "12345678956",
  "email": "teste9@email.com",
  "address": {
    "postCode": "01025020",
    "streetNumber": "321"
  }
}
```

**Address Lookup Integration:**  
When registering a client, the API uses the provided `postCode` (CEP) to automatically fetch and populate address details (such as street, neighborhood, city, and state) using the [ViaCEP API](https://viacep.com.br/). Only the `postCode` and `streetNumber` are required in the payload; the remaining address fields are completed by the service.

### **GET /v1/dealership/clients**
Retrieve a list of all clients.

### **GET /v1/dealership/clients/{id}**
Retrieves a client by ID.

### **PUT /v1/dealership/clients/{id}**
Updates an existing client.

### **DELETE /v1/dealership/clients/{id}**
Deletes a client by ID.

## API Documentation

After starting the application, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Testing
Run tests with:
```bash
mvn clean install
```

To run the integration tests, you should be with docker running. The containers will be created automatically using testcontainers. 

``