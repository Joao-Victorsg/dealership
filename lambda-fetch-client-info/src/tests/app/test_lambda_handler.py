import unittest
from unittest.mock import patch, MagicMock
from app.lambda_handler import lambda_handler
from app.utils.logger import logger
from app.adapter.dtos.client_request import ClientRequest
from app.domain.usecase.fetch_client_info import fetch_client_info

class TestLambdaHandler(unittest.TestCase):

    @patch("app.lambda_handler.fetch_client_info")
    @patch("app.lambda_handler.ClientRequest")
    @patch("app.lambda_handler.logger")
    def test_lambda_handler_success(self, mock_logger, mock_client_request, mock_fetch_client_info):
        # Mock input
        event = {"cpf": "12345678911"}
        context = {}

        # Mock behavior
        mock_client_request.return_value = MagicMock(cpf=event["cpf"])
        mock_fetch_client_info.return_value = {"client_info": "some_client_data"}

        # Call the function
        response = lambda_handler(event, context)

        # Assertions
        mock_logger.info.assert_called_with(f"Received event: {event}")
        mock_client_request.assert_called_with(cpf=event["cpf"])
        mock_fetch_client_info.assert_called_with(event["cpf"])
        self.assertEqual(response, {"client_info": "some_client_data"})

    @patch("app.lambda_handler.logger")
    def test_lambda_handler_missing_cpf(self, mock_logger):
        # Mock input
        event = {}
        context = {}

        # Call the function and assert exception
        with self.assertRaises(KeyError):
            lambda_handler(event, context)

        # Assertions
        mock_logger.error.assert_called()

    @patch("app.lambda_handler.logger")
    @patch("app.lambda_handler.ClientRequest")
    @patch("app.lambda_handler.fetch_client_info")
    def test_lambda_handler_exception(self, mock_fetch_client_info, mock_client_request, mock_logger):
        # Mock input
        event = {"cpf": "12345678911"}
        context = {}

        # Mock behavior to raise an exception
        mock_client_request.side_effect = Exception("Test exception")

        # Call the function and assert exception
        with self.assertRaises(Exception):
            lambda_handler(event, context)

        # Assertions
        mock_logger.error.assert_called()