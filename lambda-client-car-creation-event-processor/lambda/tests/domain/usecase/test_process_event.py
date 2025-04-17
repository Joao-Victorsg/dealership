import json
import pytest
from unittest.mock import MagicMock, patch
import domain.usecase.process_event as process_event_module
from domain.usecase.process_event import process_event

def test_process_known_event_type(monkeypatch):
    mock_handler = MagicMock()

    monkeypatch.setitem(process_event_module.EVENT_HANDLERS, "CarCreated", mock_handler)

    record = {
        "messageAttributes": {
            "eventType": {"stringValue": "CarCreated"}
        },
        "body": json.dumps({"vin": "ABC123"})
    }

    process_event(record)
    mock_handler.handle.assert_called_once_with({"vin": "ABC123"})


@patch("domain.usecase.process_event.logger")
def test_process_unknown_event_type(mock_logger):
    record = {
        "messageAttributes": {
            "eventType": {"stringValue": "UnknownType"}
        },
        "body": json.dumps({"foo": "bar"})
    }

    process_event(record)
    mock_logger.warning.assert_called_once_with("Unknown event type: UnknownType")


@patch("domain.usecase.process_event.logger")
def test_missing_event_type_raises_value_error(mock_logger):
    record = {
        "messageAttributes": {},  # Missing eventType
        "body": json.dumps({"some": "data"})
    }

    with pytest.raises(ValueError, match="Missing eventType"):
        process_event(record)

    assert mock_logger.error.called


@patch("domain.usecase.process_event.logger")
def test_invalid_json_raises_error(mock_logger):
    record = {
        "messageAttributes": {
            "eventType": {"stringValue": "CarCreated"}
        },
        "body": "not-a-json"
    }

    with pytest.raises(Exception):
        process_event(record)

    assert "Failed to process event" in mock_logger.error.call_args[0][0]