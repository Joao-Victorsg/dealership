from pydantic import BaseModel
from app.domain.model.address import Address

class Client(BaseModel):
    name: str
    cpf: str
    address: Address | None = None