from pydantic import BaseModel, Field
from datetime import datetime
from app.adapter.dtos.address_response import AddressResponse

class ClientResponse(BaseModel):
    name: str
    cpf: str
    address: AddressResponse
    email: str
    registrationDate: datetime