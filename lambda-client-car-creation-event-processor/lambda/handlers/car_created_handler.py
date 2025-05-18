from adapter.postgres_adapter import save_to_database
from handlers.event_handler import EventHandler

class CarCreatedHandler(EventHandler):
    def handle(self, message):
        vin = str(message)
        if not vin:
            raise ValueError("Missing VIN in CarCreated event")
        save_to_database("TB_CAR", "vin", vin)