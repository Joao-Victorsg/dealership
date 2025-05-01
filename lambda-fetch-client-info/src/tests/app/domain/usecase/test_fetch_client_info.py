import unittest
from unittest.mock import patch, MagicMock
from app.domain.usecase.fetch_client_info import fetch_client_info
from app.adapter.dtos.client_response import ClientResponse
from app.adapter.dtos.address_response import AddressResponse

class TestFetchClientInfo(unittest.TestCase):

    @patch('app.domain.usecase.fetch_client_info.get_client_by_cpf')
    def test_fetch_client_info_success(self, mock_get_client_by_cpf):
        # Arrange
        cpf = "12345678900"
        mock_address = MagicMock(spec=AddressResponse)
        mock_address.postCode = "12345-678"
        mock_address.city = "São Paulo"
        mock_address.stateAbbreviation = "SP"
        mock_address.streetName = "Rua das Flores"
        mock_address.streetNumber = "123"

        mock_client = MagicMock(spec=ClientResponse)
        mock_client.name = "João Silva"
        mock_client.cpf = cpf
        mock_client.address = mock_address
        mock_get_client_by_cpf.return_value = mock_client

        # Act
        result = fetch_client_info(cpf)

        # Assert
        expected_result = {
            "name": "João Silva",
            "cpf": cpf,
            "address": mock_address,
        }
        self.assertEqual(result, expected_result)

    @patch('app.domain.usecase.fetch_client_info.get_client_by_cpf')
    def test_fetch_client_info_failure(self, mock_get_client_by_cpf):
        # Arrange
        cpf = "12345678900"
        mock_get_client_by_cpf.side_effect = Exception("Client not found")

        # Act & Assert
        with self.assertRaises(Exception) as context:
            fetch_client_info(cpf)
        self.assertEqual(str(context.exception), "Client not found")