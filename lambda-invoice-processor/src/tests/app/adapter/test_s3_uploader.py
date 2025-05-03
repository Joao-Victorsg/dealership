import unittest
import pytest
from unittest.mock import patch, MagicMock
from app.adapter.s3_uploader import upload_invoice
from botocore.exceptions import BotoCoreError, ClientError
from app.utils.logger import logger


class TestS3Uploader(unittest.TestCase):

    @patch("app.adapter.s3_uploader.s3.put_object")
    @patch("app.utils.logger.logger.info")
    def test_upload_invoice_success(self, mock_logger_info, mock_put_object):
        # Mock successful S3 upload
        mock_put_object.return_value = None

        # Input data
        sale_id = "12345"
        invoice_html = "<html><body>Invoice</body></html>"

        # Call the function
        result = upload_invoice(sale_id, invoice_html)

        # Assertions
        expected_s3_path = "s3://InvoiceBucket/invoices/12345.html"
        self.assertEqual(result, expected_s3_path)
        mock_put_object.assert_called_once_with(
            Bucket="InvoiceBucket",
            Key="invoices/12345.html",
            Body=invoice_html,
            ContentType="text/html"
        )
        mock_logger_info.assert_called_once_with(f"Invoice uploaded to {expected_s3_path}")

    @patch("app.adapter.s3_uploader.s3.put_object")
    @patch("app.utils.logger.logger.error")
    def test_upload_invoice_failure(self, mock_logger_error, mock_put_object):
        # Mock S3 upload failure
        mock_put_object.side_effect = ClientError(
            error_response={"Error": {"Code": "500", "Message": "Internal Server Error"}},
            operation_name="PutObject"
        )

        # Input data
        sale_id = "12345"
        invoice_html = "<html><body>Invoice</body></html>"

        # Call the function and expect an exception
        with self.assertRaises(ClientError):
            upload_invoice(sale_id, invoice_html)

        # Assertions
        mock_put_object.assert_called_once_with(
            Bucket="InvoiceBucket",
            Key="invoices/12345.html",
            Body=invoice_html,
            ContentType="text/html"
        )
        mock_logger_error.assert_called_once()