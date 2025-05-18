resource "aws_iam_policy" "lambda_send_mail" {
  name        = "lambda-send-mail"
  description = "Lambda policy to send mail"
  policy      = templatefile("${path.module}/policy/policy.json", {})
}