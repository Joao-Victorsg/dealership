import json
from app.utils.logger import logger
from app.domain.model.invoice_event import InvoiceEvent

def generate_invoice(event: InvoiceEvent) -> str:
    reg_date_str = event.registrationDate.strftime("%Y-%m-%d %H:%M:%S")
    taxed_value = round(event.car.value * 1.02, 2)  # Add 2% tax and round to 2 decimal places
    
    return f"""
    <html>
      <body>
        <h1>Invoice #{event.saleId}</h1>
        <h3>Registration Date: {reg_date_str}</h3>
        <h2>Client Information</h2>
        <p><strong>Name:</strong> {event.client.name}</p>
        <p><strong>CPF:</strong> {event.client.cpf}</p>
        <p><strong>Address:</strong> {event.client.address.streetName}, {event.client.address.streetNumber}, {event.client.address.city} - {event.client.address.stateAbbreviation}, {event.client.address.postcode}</p>
        <h2>Car Information</h2>
        <p><strong>Model:</strong> {event.car.model} ({event.car.modelYear})</p>
        <p><strong>Manufacturer:</strong> {event.car.manufacturer}</p>
        <p><strong>Color:</strong> {event.car.color}</p>
        <p><strong>VIN:</strong> {event.car.vin}</p>
        <p><strong>Value:</strong> ${event.car.value:,.2f}</p>
        <h2>Total Amount</h2>
        <p><strong>Final Price (with 2% tax):</strong> ${taxed_value:,.2f}</p>
      </body>
    </html>
    """