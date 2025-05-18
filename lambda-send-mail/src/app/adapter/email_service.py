import boto3
from botocore.exceptions import BotoCoreError, ClientError
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.application import MIMEApplication
from app.config import EMAIL_FROM

ses_client = boto3.client("ses")

class EmailService:
    @staticmethod
    def send_email_with_attachment(to_address: str, subject: str, body_text: str, attachment: bytes, filename: str):
        msg = MIMEMultipart()
        msg["Subject"] = subject
        msg["From"] = EMAIL_FROM
        msg["To"] = to_address

        msg.attach(MIMEText(body_text, "plain"))

        attachment_part = MIMEApplication(attachment)
        attachment_part.add_header("Content-Disposition", "attachment", filename=filename)
        msg.attach(attachment_part)

        try:
            response = ses_client.send_raw_email(
                Source=EMAIL_FROM,
                Destinations=[to_address],
                RawMessage={"Data": msg.as_string()}
            )
            print(f"Email sent: {response['MessageId']}")
        except (BotoCoreError, ClientError) as e:
            raise RuntimeError(f"Failed to send email via SES: {e}")