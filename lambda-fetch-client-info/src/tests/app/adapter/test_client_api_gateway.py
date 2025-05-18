import unittest
from unittest.mock import patch, MagicMock
from app.adapter.client_api_gateway import get_client_by_cpf
from app.adapter.dtos.client_response import ClientResponse
from app.config import settings
from app.utils.logger import logger

class TestClientApiGateway(unittest.TestCase):

    @patch("app.adapter.client_api_gateway.session.get")
    def test_get_client_by_cpf_success(self, mock_get):
        mock_response_data = {
            "data": {
                   "name": "João Silva",
                   "cpf": "12345678900",
                   "email": "email@email.com",
                   "address": {
                       "postCode": "12345-678",
                       "city": "São Paulo",
                       "stateAbbreviation": "SP",
                       "streetName": "Rua das Flores",
                       "streetNumber": "123"
                   },
                   "registrationDate": "2020-01-01T00:00:00+00:00"
               }
        }
        mock_response = MagicMock()
        mock_response.json.return_value = mock_response_data
        mock_response.raise_for_status.return_value = None
        mock_get.return_value = mock_response

        cpf = "12345678900"
        result = get_client_by_cpf(cpf)

        self.assertIsInstance(result, ClientResponse)
        self.assertEqual(result.name, "João Silva")
        self.assertEqual(result.cpf, "12345678900")
        self.assertEqual(result.email, "email@email.com")
        self.assertEqual(result.address.postCode, "12345-678")
        self.assertEqual(result.address.city, "São Paulo")
        self.assertEqual(result.address.stateAbbreviation, "SP")
        self.assertEqual(result.address.streetName, "Rua das Flores")
        self.assertEqual(result.address.streetNumber, "123")
        self.assertEqual(result.registrationDate.isoformat(), "2020-01-01T00:00:00+00:00")
        mock_get.assert_called_once_with(
            f"{settings.CLIENT_API_URL}/{cpf}",
            timeout=settings.REQUEST_TIMEOUT
        )

    @patch("app.adapter.client_api_gateway.session.get")
    @patch("app.utils.logger.logger.error")
    def test_get_client_by_cpf_failure(self, mock_logger_error, mock_get):
        mock_get.side_effect = Exception("API error")

        cpf = "12345678900"
        with self.assertRaises(Exception) as context:
            get_client_by_cpf(cpf)

        self.assertEqual(str(context.exception), "API error")
        mock_logger_error.assert_called_once_with(
            f"Error fetching client for cpf {cpf}: API error"
        )
        mock_get.assert_called_once_with(
            f"{settings.CLIENT_API_URL}/{cpf}",
            timeout=settings.REQUEST_TIMEOUT
        )