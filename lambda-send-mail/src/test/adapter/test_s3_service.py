import pytest
from unittest.mock import patch, MagicMock
from botocore.exceptions import BotoCoreError, ClientError
from app.adapter.s3_service import S3Service

@patch("app.adapter.s3_service.s3_client")
def test_download_file_success(mock_s3_client):
    mock_body = MagicMock()
    mock_body.read.return_value = b"file-content"
    mock_s3_client.get_object.return_value = {"Body": mock_body}

    result = S3Service.download_file("test-key")
    mock_s3_client.get_object.assert_called_once_with(Bucket="test-bucket", Key="test-key")
    assert result == b"file-content"

@patch("app.adapter.s3_service.s3_client")
def test_download_file_botocore_error(mock_s3_client):
    mock_s3_client.get_object.side_effect = BotoCoreError()

    with pytest.raises(RuntimeError) as excinfo:
        S3Service.download_file("test-key")
    assert "Error downloading invoice from S3" in str(excinfo.value)

@patch("app.adapter.s3_service.s3_client")
def test_download_file_client_error(mock_s3_client):
    mock_s3_client.get_object.side_effect = ClientError(
        error_response={"Error": {"Code": "NoSuchKey", "Message": "Not found"}},
        operation_name="GetObject"
    )

    with pytest.raises(RuntimeError) as excinfo:
        S3Service.download_file("test-key")
    assert "Error downloading invoice from S3" in str(excinfo.value)