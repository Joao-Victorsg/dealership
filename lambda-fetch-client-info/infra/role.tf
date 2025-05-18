resource "aws_iam_role" "lambda_exec_role" {
  name = "lambda_exec_role_fetch_client_info"
  assume_role_policy = file("${path.module}/policy/trust-policy.json")
}

resource "aws_iam_role_policy_attachment" "lambda_role"{
  role = aws_iam_role.lambda_exec_role.name
  policy_arn = aws_iam_policy.lambda_log.arn
}