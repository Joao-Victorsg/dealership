from app.adapter.client_api_gateway import get_client_by_cpf
from app.adapter.dtos.client_response import ClientResponse
from app.utils.logger import logger

def fetch_client_info(cpf: str) -> dict:
    client: ClientResponse = get_client_by_cpf(cpf)

    logger.info(f"Fetched client info: {client}")

    return client.model_dump(exclude={'registrationDate'})