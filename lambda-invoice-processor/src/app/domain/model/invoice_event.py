from pydantic import BaseModel
from datetime import datetime
from app.domain.model.client import Client
from app.domain.model.car import Car

class InvoiceEvent(BaseModel):
    saleId: str
    client: Client
    car: Car
    registrationDate: datetime