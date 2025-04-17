from adapter.postgres_adapter import save_to_database
from handlers.event_handler import EventHandler

class ClientCreatedHandler(EventHandler):
    def handle(self, message):
        cpf = str(message)
        if not cpf:
            raise ValueError("Missing CPF in ClientCreated event")
        save_to_database("TB_CLIENT", "cpf", cpf)