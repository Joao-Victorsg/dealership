from abc import ABC, abstractmethod

class EventHandler(ABC):
    @abstractmethod
    def handle(self, message):
        raise NotImplementedError("Subclasses must implement this method")