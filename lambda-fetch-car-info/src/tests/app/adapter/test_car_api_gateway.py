import unittest
import pytest
from unittest.mock import patch, MagicMock
from app.adapter.car_api_gateway import get_car_by_vin
from app.adapter.dtos.car_response import CarResponse
from app.config import settings
from app.utils.logger import logger
from unittest import TestCase

class TestCarApiGateway(unittest.TestCase):

    @patch("app.adapter.car_api_gateway.session.get")
    def test_get_car_by_vin_success(self, mock_get):
        # Mock response data
        mock_response_data = {
            "id": "454ebb92-2cd6-49ec-8cb2-3dffa3ed071b",
            "model": "Corolla",
            "modelYear": "2020",
            "manufacturer": "Toyota",
            "color": "Blue",
            "vin": "1234567890ABCDEF",
            "value": 20000.00,
            "registrationDate": "2020-01-01T00:00:00+00:00"
        }
        mock_response = MagicMock()
        mock_response.json.return_value = mock_response_data
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        # Call the function
        vin = "1234567890ABCDEF"
        result = get_car_by_vin(vin)

        # Assertions
        self.assertIsInstance(result, CarResponse)
        self.assertEqual(str(result.id), "454ebb92-2cd6-49ec-8cb2-3dffa3ed071b")
        self.assertEqual(result.model, "Corolla")
        self.assertEqual(result.modelYear, "2020")
        self.assertEqual(result.manufacturer, "Toyota")
        self.assertEqual(result.color, "Blue")
        self.assertEqual(result.vin, "1234567890ABCDEF")
        self.assertEqual(result.value, 20000.00)
        self.assertEqual(result.registrationDate.isoformat(), "2020-01-01T00:00:00+00:00")
        mock_get.assert_called_once_with(
            f"{settings.CAR_API_URL}/{vin}",
            timeout=settings.REQUEST_TIMEOUT
        )

    @patch("app.adapter.car_api_gateway.session.get")
    @patch("app.utils.logger.logger.error")
    def test_get_car_by_vin_failure(self, mock_logger_error, mock_get):
        # Mock an exception
        mock_get.side_effect = Exception("API error")

        # Call the function and expect an exception
        vin = "1234567890ABCDEF"
        with self.assertRaises(Exception) as context:
            get_car_by_vin(vin)

        # Assertions
        self.assertEqual(str(context.exception), "API error")
        mock_logger_error.assert_called_once_with(
            f"Error fetching car for VIN {vin}: API error"
        )
        mock_get.assert_called_once_with(
            f"{settings.CAR_API_URL}/{vin}",
            timeout=settings.REQUEST_TIMEOUT
        )