import os

CLIENT_API_URL = os.getenv("CAR_API_URL", "http://host.docker.internal:8080/v1/dealership/clients")
REQUEST_TIMEOUT = int(os.getenv("REQUEST_TIMEOUT", "5"))
RETRY_TOTAL = int(os.getenv("RETRY_TOTAL", "3"))