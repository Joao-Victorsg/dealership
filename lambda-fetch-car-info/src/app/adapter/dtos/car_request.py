from pydantic import BaseModel

class CarRequest(BaseModel):
    vin: str