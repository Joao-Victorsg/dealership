import os

def pytest_configure():
    os.environ["INVOICE_BUCKET"] = "test-bucket"
    os.environ["EMAIL_FROM"] = "test@example.com"