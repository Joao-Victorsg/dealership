import pytest
from unittest.mock import patch, MagicMock
from app.lambda_handler import handler
from pydantic import ValidationError

@pytest.fixture
def valid_event():
    return {
        "client_email": "test@example.com",
        "client_name": "John Doe",
        "car_model": "Tesla Model S",
        "invoice_key": "invoices/123.pdf"
    }

@pytest.fixture
def context():
    return MagicMock()

@patch("app.lambda_handler.EmailService")
@patch("app.lambda_handler.S3Service")
@patch("app.lambda_handler.logger")
def test_handler_success(mock_logger, mock_s3_service, mock_email_service, valid_event, context):
    mock_s3_service.download_file.return_value = b"pdf-bytes"
    result = handler(valid_event, context)
    mock_logger.info.assert_any_call("Preparing to send invoice to test@example.com...")
    mock_s3_service.download_file.assert_called_once_with("invoices/123.pdf")
    mock_email_service.send_email_with_attachment.assert_called_once_with(
        to_address="test@example.com",
        subject="Your Car Purchase Invoice",
        body_text=(
            "Hi John Doe,\n\n"
            "Congratulations on your new Tesla Model S!\n"
            "Please find your invoice attached.\n\n"
            "Best regards,\nDealership Team"
        ),
        attachment=b"pdf-bytes",
        filename="invoice.pdf"
    )
    assert result == {"status": "success", "message": "Email sent successfully"}

@patch("app.lambda_handler.logger")
def test_handler_invalid_input(mock_logger, context):
    invalid_event = {
        "client_email": "test@example.com",
        # missing client_name, car_model, invoice_key
    }
    with pytest.raises(ValidationError):
        handler(invalid_event, context)
    assert mock_logger.error.call_count == 1
    assert "Invalid input" in mock_logger.error.call_args[0][0]