import pytest
from handlers.event_handler import EventHandler

class BadHandler(EventHandler):
    def handle(self, message):
        super().handle(message)  # This will raise NotImplementedError

def test_not_implemented_error():
    handler = BadHandler()
    with pytest.raises(NotImplementedError, match="Subclasses must implement this method"):
        handler.handle("some-message")