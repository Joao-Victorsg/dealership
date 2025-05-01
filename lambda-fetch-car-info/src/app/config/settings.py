import os

CAR_API_URL = os.getenv("CAR_API_URL", "http://host.docker.internal:8080/cars")
REQUEST_TIMEOUT = int(os.getenv("REQUEST_TIMEOUT", "5"))
RETRY_TOTAL = int(os.getenv("RETRY_TOTAL", "3"))