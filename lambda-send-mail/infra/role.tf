resource "aws_iam_role" "lambda_exec_role" {
  name = "lambda-send-mail-role"
  assume_role_policy = file("${path.module}/policy/trust-policy.json")
}

resource "aws_iam_role_policy_attachment" "lambda_role"{
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_send_mail.arn
}