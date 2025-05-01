resource "aws_iam_policy" "lambda_log" {
  name        = "lambda-log-policy"
  description = "Lambda policy to log"
  policy      = templatefile("${path.module}/policy/policy.json", {})
}