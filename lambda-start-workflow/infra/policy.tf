resource "aws_iam_policy" "lambda_log" {
  name        = "lambda-start-workflow-policy"
  description = "Lambda policy to start workflow"
  policy      = templatefile("${path.module}/policy/policy.json", {})
}