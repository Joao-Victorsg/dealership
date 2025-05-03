resource "aws_lambda_function" "invoice_processor" {
  function_name = "Invoice-Processor"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_invoice_processor.repository_url}:2.0"
  package_type = "Image"
}