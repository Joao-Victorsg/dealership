from app.utils.logger import logger
from app.adapter.dtos.client_request import ClientRequest
from app.domain.usecase.fetch_client_info import fetch_client_info

def lambda_handler(event, context):
    try:
        logger.info(f"Received event: {event}")
        req = ClientRequest(cpf=event["cpf"])
        return fetch_client_info(req.cpf)
    except Exception as e:
        logger.error(f"Lambda execution failed: {e}")
        raise