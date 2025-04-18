import pytest
from handlers.car_created_handler import CarCreatedHandler

import unittest
from unittest.mock import patch


class TestCarCreatedHandler(unittest.TestCase):
    
    @patch("handlers.car_created_handler.save_to_database")
    def test_handle_car_create_event(self,mock_save):
        handler = CarCreatedHandler()
        vin = "test123"
        
        handler.handle(vin)
        
        mock_save.assert_called_once_with("TB_CAR","vin",vin)
        
    @patch("handlers.car_created_handler.save_to_database")
    def test_handle_empty_message_raises_value_error(self, mock_save):
        handler = CarCreatedHandler()

        with self.assertRaises(ValueError) as context:
            handler.handle("")

        self.assertEqual(str(context.exception), "Missing VIN in CarCreated event")
        mock_save.assert_not_called()     