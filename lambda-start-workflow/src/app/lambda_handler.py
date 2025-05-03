import json
import boto3
import os

sfn_client = boto3.client("stepfunctions")

STEP_FUNCTION_ARN = os.getenv("STEP_FUNCTION_ARN")

def lambda_handler(event, context):
    for record in event["Records"]:
        sns_message = record["Sns"]["Message"]
        try:
            # Expecting the SNS message to be a JSON string
            message_data = json.loads(sns_message)
        except json.JSONDecodeError:
            print("Invalid JSON in SNS message:", sns_message)
            continue

        try:
            response = sfn_client.start_execution(
                stateMachineArn=STEP_FUNCTION_ARN,
                input=json.dumps(message_data)
            )
            print("Step Function started:", response["executionArn"])
        except Exception as e:
            print("Failed to start Step Function:", str(e))