import requests
from requests.adapters import HTTPAdapter, Retry
from app.config import settings
from app.adapter.dtos.client_response import ClientResponse
from app.utils.logger import logger


session = requests.Session()
retries = Retry(total=settings.RETRY_TOTAL, backoff_factor=1.5, status_forcelist=[500, 502, 503, 504])
adapter = HTTPAdapter(max_retries=retries)
session.mount("http://", adapter)
session.mount("https://", adapter)

def get_client_by_cpf(cpf: str) -> ClientResponse:
    url = f"{settings.CLIENT_API_URL}/{cpf}"
    logger.info(f"Requesting client info from {url}")

    try:
        response = session.get(url, timeout=settings.REQUEST_TIMEOUT)
        response.raise_for_status()
        client_data = response.json().get('data', {})

        logger.info(f"Client data received: {client_data}")

        return ClientResponse(
                    name=client_data['name'],
                    cpf=client_data['cpf'],
                    email=client_data['email'],
                    address=client_data['address'],
                    registrationDate=client_data['registrationDate']
                )
    except Exception as e:
        logger.error(f"Error fetching client for cpf {cpf}: {e}")
        raise