resource "aws_lambda_function" "start_workflow" {
  function_name = "StartWorkflow"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_start_workflow.repository_url}:latest"
  package_type = "Image"

  environment {
    variables = {
      STEP_FUNCTION_ARN = "${data.aws_sfn_state_machine.invoice_workflow.arn}"
    }
  }
}

resource "aws_lambda_permission" "allow_sns" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.start_workflow.function_name
  principal     = "sns.amazonaws.com"
  source_arn    = data.aws_sns_topic.invoice_topic.arn
}

resource "aws_sns_topic_subscription" "lambda_sub" {
  topic_arn = data.aws_sns_topic.invoice_topic.arn
  protocol  = "lambda"
  endpoint  = aws_lambda_function.start_workflow.arn
}