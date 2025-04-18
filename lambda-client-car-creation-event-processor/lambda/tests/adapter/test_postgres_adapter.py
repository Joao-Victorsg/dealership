import pytest
from unittest.mock import patch, MagicMock
from adapter.postgres_adapter import save_to_database, get_db_connection


# ✅ Test get_db_connection success
@patch("adapter.postgres_adapter.psycopg.connect")
def test_get_db_connection_success(mock_connect):
    conn_mock = MagicMock()
    mock_connect.return_value = conn_mock

    result = get_db_connection()

    mock_connect.assert_called_once()
    assert result == conn_mock


# ✅ Test get_db_connection failure
@patch("adapter.postgres_adapter.psycopg.connect", side_effect=Exception("connection failed"))
@patch("adapter.postgres_adapter.logger")
def test_get_db_connection_failure(mock_logger, mock_connect):
    with pytest.raises(Exception, match="connection failed"):
        get_db_connection()

    mock_logger.error.assert_called_once()
    mock_connect.assert_called_once()


# ✅ Test save_to_database success
@patch("adapter.postgres_adapter.get_db_connection")
@patch("adapter.postgres_adapter.logger")
def test_save_to_database_success(mock_logger, mock_get_conn):
    mock_conn = MagicMock()
    mock_cursor = MagicMock()

    # Set up context managers
    mock_conn.__enter__.return_value = mock_conn
    mock_conn.cursor.return_value.__enter__.return_value = mock_cursor

    mock_get_conn.return_value = mock_conn

    save_to_database("TB_CAR", "vin", "ABC123")

    mock_get_conn.assert_called_once()
    mock_cursor.execute.assert_called_once_with(
        "INSERT INTO TB_CAR (vin) VALUES (%s) ON CONFLICT DO NOTHING;",
        ("ABC123",)
    )
    mock_conn.commit.assert_called_once()
    mock_logger.info.assert_called_once_with(
        "Successfully inserted vin=ABC123 into TB_CAR table"
    )


# ✅ Test save_to_database failure
@patch("adapter.postgres_adapter.get_db_connection", side_effect=Exception("db down"))
@patch("adapter.postgres_adapter.logger")
def test_save_to_database_failure(mock_logger, mock_get_conn):
    with pytest.raises(Exception, match="db down"):
        save_to_database("TB_CAR", "vin", "XYZ")

    mock_logger.error.assert_called_once_with(
        "Database insert error for TB_CAR: db down"
    )