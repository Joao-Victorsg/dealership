# Client API

## Description
The **Client API** is a Java-based service built with the Spring Boot framework. It provides functionalities to manage client information, including CPF, name, address, email, and registration date. The application is packaged as a JAR file, runs in a Docker container, and is designed for deployment on AWS ECS using Fargate. A Swagger page is available for API documentation.

## Technologies Used
- **Java 21**
- **Spring Boot**
- **Maven**
- **Docker**
- **AWS ECS with Fargate**
- **Terraform** for infrastructure as code
- **Aurora PostgreSQL** for database management
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
- PostgreSQL database

### Clone the Repository
```bash
git clone https://github.com/joaovictorsg/client-api.git
cd client-api
```

### Configuration

Configure the application via `src/main/resources/application.properties` or environment variables. Main properties:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `server.port`

Example:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clientdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
server.port=8080
```

### Environment Variables

You can override configuration using environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT`

## Build & Run

### Locally
```bash
mvn clean package
java -jar target/client-api-*.jar
```

### With Docker
Build the Docker image:
```bash
docker build -t client-api .
```
Run the container:
```bash
docker run -p 8080:8080 --env-file .env client-api
```

### With LocalStack (for local AWS services)
To run the application locally emulating AWS services, use [LocalStack](https://github.com/localstack/localstack):

```bash
# Start LocalStack
docker-compose -f infra/localstack/docker-compose.yml up -d

# Set AWS credentials for LocalStack (if needed)
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1

# Run the application
mvn spring-boot:run
```

## Deployment

The application is designed for deployment on AWS ECS using Fargate. Infrastructure can be provisioned using Terraform scripts provided in the `infra/` directory.

## Main Endpoints
The API exposes the following endpoints. Full documentation is available at the Swagger page: `/swagger-ui.html`.

### **GET /v1/dealership/actuator/health**
Checks the application's health status.

### **POST /v1/dealership/clients**
Creates a new client.

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

## Contributing

1. Fork this repository
2. Create a new branch: `git checkout -b feature/your-feature`
3. Make your changes and commit: `git commit -am 'Add new feature'`
4. Push to your branch: `git push origin feature/your-feature`
5. Open a Pull Request

## License

This project is licensed under the MIT License.

``