resource "aws_lambda_function" "send_mail" {
  function_name = "send-mail"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_send_mail.repository_url}:latest"
  package_type = "Image"

  environment {
    variables = {
      INVOICE_BUCKET = "invoicebucket"
      EMAIL_FROM = "noreply@example.com"
    }
  }
}