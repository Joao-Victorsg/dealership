import json

from domain.usecase import process_event
from utils.logger import logger

def lambda_handler(event, context):
    try:
        for record in event["Records"]:
            process_event(record)

        return {"statusCode": 200, "body": json.dumps("Success")}

    except Exception as e:
        logger.error(f"Lambda function error: {e}")
        return {"statusCode": 500, "body": json.dumps("Internal Server Error")}
