import pytest
from unittest.mock import patch, MagicMock
from app.adapter.email_service import EmailService
from botocore.exceptions import ClientError

@patch("app.adapter.email_service.ses_client")
def test_send_email_with_attachment_success(mock_ses_client):
    mock_send_raw_email = MagicMock(return_value={"MessageId": "12345"})
    mock_ses_client.send_raw_email = mock_send_raw_email

    to_address = "recipient@example.com"
    subject = "Test Subject"
    body_text = "Test Body"
    attachment = b"Test Attachment Content"
    filename = "test.txt"

    EmailService.send_email_with_attachment(
        to_address, subject, body_text, attachment, filename
    )

    assert mock_send_raw_email.called
    args, kwargs = mock_send_raw_email.call_args
    assert kwargs["Source"] == "test@example.com"
    assert kwargs["Destinations"] == [to_address]
    assert "RawMessage" in kwargs
    assert subject in kwargs["RawMessage"]["Data"]
    assert body_text in kwargs["RawMessage"]["Data"]
    assert filename in kwargs["RawMessage"]["Data"]

@patch("app.adapter.email_service.ses_client")
def test_send_email_with_attachment_failure(mock_ses_client):

    mock_ses_client.send_raw_email.side_effect = ClientError(
        error_response={"Error": {"Code": "MockError", "Message": "Mocked error"}},
        operation_name="SendRawEmail"
    )

    to_address = "recipient@example.com"
    subject = "Test Subject"
    body_text = "Test Body"
    attachment = b"Test Attachment Content"
    filename = "test.txt"

    with pytest.raises(RuntimeError) as excinfo:
        EmailService.send_email_with_attachment(
            to_address, subject, body_text, attachment, filename
        )
    assert "Failed to send email via SES" in str(excinfo.value)