resource "aws_iam_policy" "lambda_invoice_processor" {
  name        = "lambda-invoice-processor"
  description = "Lambda policy to invoice processor"
  policy      = templatefile("${path.module}/policy/policy.json", {})
}