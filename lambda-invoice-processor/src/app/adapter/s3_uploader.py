import boto3
from botocore.exceptions import BotoCoreError, ClientError
from app.utils.logger import logger

s3 = boto3.client("s3")
BUCKET_NAME = "InvoiceBucket"

def upload_invoice(sale_id: str, invoice_html: str) -> str:
    key = f"invoices/{sale_id}.html"
    try:
        s3.put_object(
            Bucket=BUCKET_NAME,
            Key=key,
            Body=invoice_html,
            ContentType="text/html"
        )
        logger.info(f"Invoice uploaded to s3://{BUCKET_NAME}/{key}")
        return f"s3://{BUCKET_NAME}/{key}"
    except (BotoCoreError, ClientError) as e:
        logger.error(f"Failed to upload invoice: {e}")
        raise