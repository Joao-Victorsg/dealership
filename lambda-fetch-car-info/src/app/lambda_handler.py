from app.utils.logger import logger
from app.adapter.dtos.car_request import CarRequest
from app.domain.usecase.fetch_car_info import fetch_car_info

def lambda_handler(event, context):
    try:
        logger.info(f"Received event: {event}")
        req = CarRequest(vin=event["vin"])
        return fetch_car_info(req.vin)
    except Exception as e:
        logger.error(f"Lambda execution failed: {e}")
        raise