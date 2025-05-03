import json

from app.utils.logger import logger
from app.domain.model.invoice_event import InvoiceEvent
from app.adapter.s3_uploader import upload_invoice
from app.domain.usecase.generate_invoice import generate_invoice

def lambda_handler(event, context):
    try:
        logger.info(f"Received event: {json.dumps(event)}")
        
        # Validate and parse the event payload using InvoiceEvent model
        invoice_data = InvoiceEvent(**event)
        
        # Generate invoice HTML content using the use-case logic
        invoice_html = generate_invoice(invoice_data)
        
        # Upload the invoice HTML to S3
        s3_url = upload_invoice(invoice_data.saleId, invoice_html)
        
        return {
            "statusCode": 200,
            "body": json.dumps({
                "message": "Invoice generated successfully",
                "s3_url": s3_url
            })
        }
    except Exception as e:
        logger.exception("Error generating invoice")
        return {
            "statusCode": 500,
            "body": json.dumps({
                "message": "Internal server error",
                "error": str(e)
            })
        }