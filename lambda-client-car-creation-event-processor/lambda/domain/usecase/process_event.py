import json
from utils.logger import logger
from handlers.car_created_handler import CarCreatedHandler
from handlers.client_created_handler import ClientCreatedHandler

# Strategy mapping
EVENT_HANDLERS = {
    "CarCreated": CarCreatedHandler(),
    "ClientCreated": ClientCreatedHandler(),
}

def process_event(record):
    try:
        event_type = record["messageAttributes"].get("eventType", {}).get("stringValue")

        if not event_type:
            raise ValueError("Missing eventType in SNS headers")

        message = json.loads(record["body"])
        handler = EVENT_HANDLERS.get(event_type)

        if handler:
            handler.handle(message)
        else:
            logger.warning(f"Unknown event type: {event_type}")

    except Exception as e:
        logger.error(f"Failed to process event: {record}. Error: {e}")
        raise