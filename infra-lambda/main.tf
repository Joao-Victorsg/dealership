resource "archive_file" "python_lambda_package"{
  type = "zip"
  source_file = "D:\\JV\\Projetos\\lambda_invoice\\lambda_function.py"
  output_path = "lambda.zip"
}

resource "aws_lambda_function" "client_car_creation_event_processor" {
  function_name = "Client-Car-Creation-Event-Processor"
  filename = ""
  role          = ""
  source_code_hash = archive_file.python_lambda_package.output_base64sha256
  runtime = "python3.11"
  handler = "lambda_function.lambda_handler"
}