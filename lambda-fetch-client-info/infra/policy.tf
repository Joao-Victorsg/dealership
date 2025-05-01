resource "aws_iam_policy" "lambda_log" {
  name        = "lambda-client-log-policy"
  description = "Lambda policy to log"
  policy      = templatefile("${path.module}/policy/policy.json", {})
}