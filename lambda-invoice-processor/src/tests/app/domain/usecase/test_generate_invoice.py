import unittest
from app.domain.usecase.generate_invoice import generate_invoice
from app.domain.model.invoice_event import InvoiceEvent
from datetime import datetime
from unittest.mock import MagicMock


class TestGenerateInvoice(unittest.TestCase):

    def test_generate_invoice_success(self):
        # Mock input data
        mock_event = MagicMock()
        mock_event.saleId = "12345"
        mock_event.registrationDate = datetime(2023, 5, 1, 12, 0, 0)
        mock_event.client.name = "João Silva"
        mock_event.client.cpf = "12345678900"
        mock_event.client.address.streetName = "Rua das Flores"
        mock_event.client.address.streetNumber = "123"
        mock_event.client.address.city = "São Paulo"
        mock_event.client.address.stateAbbreviation = "SP"
        mock_event.client.address.postcode = "12345-678"
        mock_event.car.model = "Corolla"
        mock_event.car.modelYear = "2020"
        mock_event.car.manufacturer = "Toyota"
        mock_event.car.color = "Blue"
        mock_event.car.vin = "1HGCM82633A123456"
        mock_event.car.value = 20000.00

        # Call the function
        result = generate_invoice(mock_event)

        # Assertions
        self.assertIn("<h1>Invoice #12345</h1>", result)
        self.assertIn("<h3>Registration Date: 2023-05-01 12:00:00</h3>", result)
        self.assertIn("<h2>Client Information</h2>", result)
        self.assertIn("<p><strong>Name:</strong> João Silva</p>", result)
        self.assertIn("<p><strong>CPF:</strong> 12345678900</p>", result)
        self.assertIn("<p><strong>Address:</strong> Rua das Flores, 123, São Paulo - SP, 12345-678</p>", result)
        self.assertIn("<h2>Car Information</h2>", result)
        self.assertIn("<p><strong>Model:</strong> Corolla (2020)</p>", result)
        self.assertIn("<p><strong>Manufacturer:</strong> Toyota</p>", result)
        self.assertIn("<p><strong>Color:</strong> Blue</p>", result)
        self.assertIn("<p><strong>VIN:</strong> 1HGCM82633A123456</p>", result)
        self.assertIn("<p><strong>Value:</strong> $20,000.00</p>", result)
        self.assertIn("<h2>Total Amount</h2>", result)
        self.assertIn("<p><strong>Final Price (with 2% tax):</strong> $20,400.00</p>", result)