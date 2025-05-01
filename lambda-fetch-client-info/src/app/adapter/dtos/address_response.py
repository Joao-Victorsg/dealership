from pydantic import BaseModel, Field

class AddressResponse(BaseModel):
    postCode: str
    city: str
    stateAbbreviation: str
    streetName: str
    streetNumber: str