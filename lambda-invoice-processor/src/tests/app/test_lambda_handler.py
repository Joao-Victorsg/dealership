import unittest
import json
from unittest.mock import patch, MagicMock
from app.lambda_handler import lambda_handler


class TestLambdaHandler(unittest.TestCase):

    @patch("app.lambda_handler.upload_invoice")
    @patch("app.lambda_handler.generate_invoice")
    @patch("app.lambda_handler.InvoiceEvent")
    @patch("app.lambda_handler.logger")
    def test_lambda_handler_success(self, mock_logger, mock_invoice_event, mock_generate_invoice, mock_upload_invoice):
        # Mock input event
        event = {
            "saleId": "12345",
            "registrationDate": "2023-05-01T12:00:00",
            "client": {
                "name": "João Silva",
                "cpf": "12345678900",
                "address": {
                    "streetName": "Rua das Flores",
                    "streetNumber": "123",
                    "city": "São Paulo",
                    "stateAbbreviation": "SP",
                    "postcode": "12345-678"
                }
            },
            "car": {
                "model": "Corolla",
                "modelYear": "2020",
                "manufacturer": "Toyota",
                "color": "Blue",
                "vin": "1HGCM82633A123456",
                "value": 20000.00
            }
        }
        context = {}

        # Mock behavior
        mock_invoice_event.return_value = MagicMock()
        mock_invoice_event.return_value.saleId = "12345"  # Set the saleId explicitly
        mock_generate_invoice.return_value = "<html>Invoice HTML</html>"
        mock_upload_invoice.return_value = "s3://bucket/invoices/12345.html"

        # Call the function
        response = lambda_handler(event, context)

        # Assertions
        mock_logger.info.assert_called_with(f"Received event: {json.dumps(event)}")
        mock_invoice_event.assert_called_with(**event)
        mock_generate_invoice.assert_called_once_with(mock_invoice_event.return_value)
        mock_upload_invoice.assert_called_once_with("12345", "<html>Invoice HTML</html>")
        self.assertEqual(response["statusCode"], 200)
        self.assertIn("Invoice generated successfully", response["body"])
        self.assertIn("s3://bucket/invoices/12345.html", response["body"])

    @patch("app.lambda_handler.logger")
    def test_lambda_handler_failure(self, mock_logger):
        # Mock input event with missing fields
        event = {"invalid": "data"}
        context = {}

        # Call the function
        response = lambda_handler(event, context)

        # Assertions
        mock_logger.exception.assert_called_once_with("Error generating invoice")
        self.assertEqual(response["statusCode"], 500)
        self.assertIn("Internal server error", response["body"])