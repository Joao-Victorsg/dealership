resource "aws_s3_bucket" "invoice_bucket" {
  bucket = "invoicebucket"  # must be globally unique!

  tags = {
    Name        = "InvoiceBucket"
    Environment = "dev"
  }
}

resource "aws_s3_bucket_versioning" "invoice_bucket_versioning" {
  bucket = aws_s3_bucket.invoice_bucket.id

  versioning_configuration {
    status = "Enabled"
  }
}

resource "aws_s3_bucket_public_access_block" "invoice_bucket_public_access" {
  bucket = aws_s3_bucket.invoice_bucket.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}
