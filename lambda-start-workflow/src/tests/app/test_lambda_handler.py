import unittest
from unittest.mock import patch, MagicMock
import json

# Mock os.getenv before importing the lambda_handler module
with patch("os.getenv", return_value="arn:aws:states:us-east-1:123456789012:stateMachine:TestStateMachine"):
    from app.lambda_handler import lambda_handler


class TestLambdaHandler(unittest.TestCase):

    @patch("app.lambda_handler.sfn_client.start_execution")
    def test_lambda_handler_success(self, mock_start_execution):
        # Mock input event
        event = {
            "Records": [
                {
                    "Sns": {
                        "Message": json.dumps({"key": "value"})
                    }
                }
            ]
        }
        context = {}

        # Mock Step Functions response
        mock_start_execution.return_value = {"executionArn": "arn:aws:states:us-east-1:123456789012:execution:TestExecution"}

        # Call the function
        lambda_handler(event, context)

        # Assertions
        mock_start_execution.assert_called_once_with(
            stateMachineArn="arn:aws:states:us-east-1:123456789012:stateMachine:TestStateMachine",
            input=json.dumps({"key": "value"})
        )

    @patch("app.lambda_handler.sfn_client.start_execution")
    def test_lambda_handler_invalid_json(self, mock_start_execution):
        # Mock input event with invalid JSON
        event = {
            "Records": [
                {
                    "Sns": {
                        "Message": "Invalid JSON"
                    }
                }
            ]
        }
        context = {}

        # Call the function
        lambda_handler(event, context)

        # Assertions
        mock_start_execution.assert_not_called()

    @patch("app.lambda_handler.sfn_client.start_execution")
    def test_lambda_handler_step_function_failure(self, mock_start_execution):
        # Mock input event
        event = {
            "Records": [
                {
                    "Sns": {
                        "Message": json.dumps({"key": "value"})
                    }
                }
            ]
        }
        context = {}

        # Mock Step Functions failure
        mock_start_execution.side_effect = Exception("Step Function error")

        # Call the function
        lambda_handler(event, context)

        # Assertions
        mock_start_execution.assert_called_once_with(
            stateMachineArn="arn:aws:states:us-east-1:123456789012:stateMachine:TestStateMachine",
            input=json.dumps({"key": "value"})
        )


if __name__ == "__main__":
    unittest.main()