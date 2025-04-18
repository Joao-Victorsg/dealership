resource "aws_iam_policy" "lambda_sqs_access" {
  name        = "creation-event-lambda-policy"
  description = "Lambda policy to access the SQS queue"
  policy      = templatefile("${path.module}/policy/policy.json", {
    queue_arn = data.aws_sqs_queue.creation_event_queue.arn,
    aws_region = "us-east-1",
    aws_account_id = data.aws_caller_identity.current.account_id
  })
}