from pydantic import BaseModel

class Car(BaseModel):
    model: str
    modelYear: int
    manufacturer: str
    color: str
    vin: str
    value: float | None = None