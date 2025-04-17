resource "aws_lambda_function" "client_car_creation_event_processor" {
  function_name = "Client-Car-Creation-Event-Processor"
  filename = "..\\lambda\\lambda.zip"
  role          = aws_iam_role.lambda_exec_role.arn
  source_code_hash = filebase64sha256("..\\lambda\\lambda.zip")
  runtime = "python3.11"
  handler = "lambda_handler.lambda_handler"

  environment {
    variables = {
      DB_HOST = "localhost.localstack.cloud"
      DB_USER = data.aws_ssm_parameter.database_username.value
      DB_PASSWORD = data.aws_secretsmanager_secret_version.database_secret_version.secret_string
      DB_NAME = "dealershipdb"
      DB_PORT = 4512
    }
  }
}

resource "aws_lambda_event_source_mapping" "lambda_sqs_trigger"{
  event_source_arn = data.aws_sqs_queue.creation_event_queue.arn
  function_name = aws_lambda_function.client_car_creation_event_processor.arn
  batch_size = 1 #Only to test initially
  enabled = true
}