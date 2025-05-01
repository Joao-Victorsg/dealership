import requests
from requests.adapters import HTTPAdapter, Retry
from app.config import settings
from app.adapter.dtos.car_response import CarResponse
from app.utils.logger import logger


session = requests.Session()
retries = Retry(total=settings.RETRY_TOTAL, backoff_factor=1.5, status_forcelist=[500, 502, 503, 504])
adapter = HTTPAdapter(max_retries=retries)
session.mount("http://", adapter)
session.mount("https://", adapter)

def get_car_by_vin(vin: str) -> CarResponse:
    url = f"{settings.CAR_API_URL}/{vin}"
    logger.info(f"Requesting car info from {url}")

    try:
        response = session.get(url, timeout=settings.REQUEST_TIMEOUT)
        response.raise_for_status()
        car_data = response.json()
        return CarResponse(**car_data)
    except Exception as e:
        logger.error(f"Error fetching car for VIN {vin}: {e}")
        raise