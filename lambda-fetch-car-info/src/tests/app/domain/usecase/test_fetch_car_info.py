import unittest
from unittest.mock import patch, MagicMock
from app.domain.usecase.fetch_car_info import fetch_car_info
from app.adapter.dtos.car_response import CarResponse

class TestFetchCarInfo(unittest.TestCase):

    @patch('app.domain.usecase.fetch_car_info.get_car_by_vin')
    def test_fetch_car_info_success(self, mock_get_car_by_vin):
        # Arrange
        vin = "1HGCM82633A123456"
        mock_car = MagicMock(spec=CarResponse)
        mock_car.model = "Civic"
        mock_car.modelYear = 2020
        mock_car.manufacturer = "Honda"
        mock_car.color = "Blue"
        mock_car.vin = vin
        mock_car.value = 20000.0
        mock_get_car_by_vin.return_value = mock_car

        # Act
        result = fetch_car_info(vin)

        # Assert
        expected_result = {
            "model": "Civic",
            "modelYear": 2020,
            "manufacturer": "Honda",
            "color": "Blue",
            "vin": vin,
            "value": 20000.0
        }
        self.assertEqual(result, expected_result)

    @patch('app.domain.usecase.fetch_car_info.get_car_by_vin')
    def test_fetch_car_info_with_none_value(self, mock_get_car_by_vin):
        # Arrange
        vin = "1HGCM82633A123456"
        mock_car = MagicMock(spec=CarResponse)
        mock_car.model = "Civic"
        mock_car.modelYear = 2020
        mock_car.manufacturer = "Honda"
        mock_car.color = "Blue"
        mock_car.vin = vin
        mock_car.value = None
        mock_get_car_by_vin.return_value = mock_car

        # Act
        result = fetch_car_info(vin)

        # Assert
        expected_result = {
            "model": "Civic",
            "modelYear": 2020,
            "manufacturer": "Honda",
            "color": "Blue",
            "vin": vin,
            "value": None
        }
        self.assertEqual(result, expected_result)