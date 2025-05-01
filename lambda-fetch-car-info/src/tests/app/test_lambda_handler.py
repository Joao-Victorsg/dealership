import unittest
from unittest.mock import patch, MagicMock
from app.lambda_handler import lambda_handler
from app.utils.logger import logger
from app.adapter.dtos.car_request import CarRequest
from app.domain.usecase.fetch_car_info import fetch_car_info

class TestLambdaHandler(unittest.TestCase):

    @patch("app.lambda_handler.fetch_car_info")
    @patch("app.lambda_handler.CarRequest")
    @patch("app.lambda_handler.logger")
    def test_lambda_handler_success(self, mock_logger, mock_car_request, mock_fetch_car_info):
        # Mock input
        event = {"vin": "1HGCM82633A123456"}
        context = {}

        # Mock behavior
        mock_car_request.return_value = MagicMock(vin=event["vin"])
        mock_fetch_car_info.return_value = {"car_info": "some_car_data"}

        # Call the function
        response = lambda_handler(event, context)

        # Assertions
        mock_logger.info.assert_called_with(f"Received event: {event}")
        mock_car_request.assert_called_with(vin=event["vin"])
        mock_fetch_car_info.assert_called_with(event["vin"])
        self.assertEqual(response, {"car_info": "some_car_data"})

    @patch("app.lambda_handler.logger")
    def test_lambda_handler_missing_vin(self, mock_logger):
        # Mock input
        event = {}
        context = {}

        # Call the function and assert exception
        with self.assertRaises(KeyError):
            lambda_handler(event, context)

        # Assertions
        mock_logger.error.assert_called()

    @patch("app.lambda_handler.logger")
    @patch("app.lambda_handler.CarRequest")
    @patch("app.lambda_handler.fetch_car_info")
    def test_lambda_handler_exception(self, mock_fetch_car_info, mock_car_request, mock_logger):
        # Mock input
        event = {"vin": "1HGCM82633A123456"}
        context = {}

        # Mock behavior to raise an exception
        mock_car_request.side_effect = Exception("Test exception")

        # Call the function and assert exception
        with self.assertRaises(Exception):
            lambda_handler(event, context)

        # Assertions
        mock_logger.error.assert_called()