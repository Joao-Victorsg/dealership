from app.adapter.client_api_gateway import get_client_by_cpf
from app.adapter.dtos.client_response import ClientResponse

def fetch_client_info(cpf: str) -> dict:
    client: ClientResponse = get_client_by_cpf(cpf)
    return {
        "name": client.name,
        "cpf": client.cpf,
        "address": client.address,
    }