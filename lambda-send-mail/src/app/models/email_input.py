from pydantic import BaseModel, EmailStr

class EmailInput(BaseModel):
    invoice_key: str
    client_email: EmailStr
    client_name: str
    car_model: str