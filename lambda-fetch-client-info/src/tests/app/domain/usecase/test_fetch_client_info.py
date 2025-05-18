import unittest
from unittest.mock import patch, MagicMock
from app.domain.usecase.fetch_client_info import fetch_client_info
from app.adapter.dtos.client_response import ClientResponse
from app.adapter.dtos.address_response import AddressResponse

class TestFetchClientInfo(unittest.TestCase):

    @patch('app.domain.usecase.fetch_client_info.get_client_by_cpf')
    def test_fetch_client_info_success(self, mock_get_client_by_cpf):
        cpf = "12345678900"
        mock_address = AddressResponse(
            postCode="12345-678",
            city="São Paulo",
            stateAbbreviation="SP",
            streetName="Rua das Flores",
            streetNumber="123"
        )

        mock_client = ClientResponse(
            name="João Silva",
            cpf=cpf,
            email="email@email.com",
            address=mock_address,
            registrationDate="2020-01-01T00:00:00+00:00"  # inclua a data de registro, já que é um campo obrigatório
        )

        mock_get_client_by_cpf.return_value = mock_client

        result = fetch_client_info(cpf)

        expected_result = {
            "name": "João Silva",
            "cpf": cpf,
            "email": "email@email.com",
            "address": mock_address.model_dump()
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