from app.models import EmailInput
from app.adapter.s3_service import S3Service
from app.adapter.email_service import EmailService
from app.utils.logger import logger
from pydantic import ValidationError
from typing import Dict

def lambda_handler(event: Dict, context):
    try:
        input_data = EmailInput(**event)
    except ValidationError as e:
        logger.error(f"Invalid input: {e}")
        raise

    logger.info(f"Preparing to send invoice to {input_data.client_email}...")

    invoice_data = S3Service.download_file(input_data.invoice_key)

    subject = "Your Car Purchase Invoice"
    
    body = (
        f"Hi {input_data.client_name},\n\n"
        f"Congratulations on your new {input_data.car_model}!\n"
        f"Please find your invoice attached.\n\n"
        f"Best regards,\nDealership Team"
    )

    EmailService.send_email_with_attachment(
        to_address=input_data.client_email,
        subject=subject,
        body_text=body,
        attachment=invoice_data,
        filename="invoice.html"
    )

    return {"status": "success", "message": "Email sent successfully"}