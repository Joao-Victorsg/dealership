from pydantic import BaseModel

class ClientRequest(BaseModel):
    cpf: str