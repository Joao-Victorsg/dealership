from pydantic import BaseModel

class Address(BaseModel):
    postcode: str
    city: str
    stateAbbreviation: str
    streetName: str
    streetNumber: str