from pydantic import BaseModel

class Address(BaseModel):
    postCode: str
    city: str
    stateAbbreviation: str
    streetName: str
    streetNumber: str