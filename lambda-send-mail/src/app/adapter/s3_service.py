import boto3
from botocore.exceptions import BotoCoreError, ClientError
from app.config import INVOICE_BUCKET

s3_client = boto3.client("s3")

class S3Service:
    @staticmethod
    def download_file(key: str) -> bytes:
        try:
            response = s3_client.get_object(Bucket=INVOICE_BUCKET, Key=key)
            return response["Body"].read()
        except (BotoCoreError, ClientError) as e:
            raise RuntimeError(f"Error downloading invoice from S3: {e}")