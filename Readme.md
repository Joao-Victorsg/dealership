# Dealership Microservices

*** Still in progress ***

This is a study project that demonstrates a microservices-based cloud architecture for a car dealership system using AWS services. The architecture is designed with event-driven components, infrastructure-as-code using Terraform, and services deployed locally using LocalStack.

The system supports CRUD operations for **Clients**, **Cars**, and **Sales**, and includes an event-driven invoice generation flow integrated with **Step Functions**, **Lambda**, **S3**, and **SES**.

## Architecture Overview

The system consists of three core services:

- **Client Service**: Manages customer information.
- **Car Service**: Manages vehicle data.
- **Sales Service**: Handles sales transactions and invoice generation.

The services communicate through asynchronous events using SNS topics and queues. Each service stores its own data in an isolated database. Upon registering a sale, the system triggers a sequence of steps to generate and send an invoice.

![Dealership Architecture](Arquitetura-Dealership-novo-Dealership%20V3-Dealership%20V3.drawio.png)

## How It Works

### Client and Car Registration

- When a client or car is created, their respective service publishes an event to an SQS queue.
- A Lambda function subscribed to the queue processes the event and stores the necessary information in the Sales database (e.g., CPF, VIN).

### Sale and Invoice Generation

- When a sale is created, the Sales service publishes an event to an SNS topic.
- A Lambda Function subscribed to the topic processes the event and triggers a Step Function.
- The Step Function is triggered to:
    1. Fetch detailed data about the client and car from their respective APIs.
    2. Generate invoice and store it in S3.
    3. Send the invoice to the client via SES using another Lambda function.

This approach decouples the services and uses event orchestration for automation.

---

## Project Structure

As the project is a monorepo, the directories are structured in a way that each service has its own directory. The idea to be a monorepo is to 
facilitate the deployment and the management of the services.

Normally, if the project wasn't a study one, I would choose to each service have its own repository.

Another detail is that the infra directories contain the terraform files to create the infrastructure in localstack.

## Technologies
- **Languages**: Java 21 (Spring Boot), Python 3.11
- **Infrastructure**: Terraform, Docker
- **AWS Services** (via LocalStack):
    - API Gateway, ECS, ECR, S3, RDS (PostgreSQL), SQS, SNS, Lambda, Step Functions, SES
    - CloudWatch, Secrets Manager, Systems Manager (SSM), VPC, NLB
- **Local Tools**:
    - LocalStack (local AWS simulation)
    - SMTP4dev (email testing)

---

## Running the project locally

Some AWS services used require LocalStack Pro. For study projects, LocalStack provides a free Pro access token.

### 1. Get the LocalStack Pro Access Key

You can get a free access key for educational use from [here](https://blog.localstack.cloud/introducing-localstack-new-aws-tiers-expanded-snowflake-support/).

### 2. Export the Key

Set the access token as an environment variable:

```bash
export LOCALSTACK_AUTH_TOKEN=your_token_here
```
### 3. Deploy the environment

```bash
 ./localstack-dealership/deploy-all.sh
```

This script will create all the necessary infra resources in localstack and deploy the applications.

### 4. API Testing

A [Postman collection](dealership.postman_collection.json) is included at the root folder of the project.

It provides pre-configured requests for testing the Client, Car, and Sales services.

Each service directory also includes its own README.md with specific API endpoint documentation.

You can verify invoice delivery using the SMTP4dev interface at http://localhost:5000.

### 5. Design Considerations

- **One API Microservice per Domain**: In a simple domain like that and depending on the volume, I probably would decide to have only one API for all the domains. But I decided to create one API for each domain to go with a more microservices approach, providing more flexibility to scaling each of the domains independently.
- **Event Orchestration with Step Functions**: I think that it would be possible to do the invoice generation without step functions, relying only on event drive architecture diminishing a vendor lock in. But as I don't have that much of experience with step function, I decided to use it to study. So I used it to coordinate the workflow between the lambdas and to merge some information before some of the lambda calls.
- **Decoupling the databases**: I decided to use a different database for each microservice to stick with the microservice approach.
- **Using events to decouple the services**: Only the necessary Client (CPF) and Car (VIN) data are asynchronously replicated into the Sales database using Lambda and SQS. This approach minimizes coupling between services and ensures that the Sales service can continue to function even if the Client or Car services are temporarily unavailable.
 
