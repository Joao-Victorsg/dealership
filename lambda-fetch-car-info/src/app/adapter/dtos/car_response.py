from pydantic import BaseModel, Field
from datetime import datetime
from uuid import UUID
from typing import Optional
from decimal import Decimal

class CarResponse(BaseModel):
    id: UUID
    model: str
    modelYear: str
    manufacturer: str
    color: str
    vin: str
    value: Optional[Decimal] = None
    registrationDate: datetime