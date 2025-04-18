import pytest
from handlers.client_created_handler import ClientCreatedHandler

import unittest
from unittest.mock import patch

class TestClientCreatedHandler(unittest.TestCase):
    
    @patch("handlers.client_created_handler.save_to_database")
    def test_handle_client_create_event(self,mock_save):
        handler = ClientCreatedHandler()
        cpf = "1234567789101"
        
        handler.handle(cpf)
        
        mock_save.assert_called_once_with("TB_CLIENT", "cpf", cpf)
        
    @patch("handlers.client_created_handler.save_to_database")
    def test_handle_empty_message_raises_value_error(self, mock_save):
        handler = ClientCreatedHandler()
        
        with self.assertRaises(ValueError) as context:
            handler.handle("")
            
        self.assertEqual(str(context.exception), "Missing CPF in ClientCreated event")
        mock_save.assert_not_called()        