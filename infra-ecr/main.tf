resource "aws_ecr_repository" "lambda-creation-event-processor" {
  name = "joaovictorsg/lambda-creation-event-processor"
}

resource "aws_ecr_repository" "lambda_fetch_client_info" {
  name = "joaovictorsg/lambda-fetch-client-info"
}

resource "aws_ecr_repository" "lambda_fetch_car_info" {
  name = "joaovictorsg/lambda-fetch-car-info"
}

resource "aws_ecr_repository" "lambda_invoice_processor" {
  name = "joaovictorsg/lambda-invoice-processor"
}

resource "aws_ecr_repository" "lambda_start_workflow" {
  name = "joaovictorsg/lambda-start-workflow"
}

resource "aws_ecr_repository" "lambda_send_mail" {
  name = "joaovictorsg/lambda-send-mail"
}