from abc import abstractmethod

class EventHandler:
    @abstractmethod
    def handle(self, message):
        raise NotImplementedError("Subclasses must implement this method")