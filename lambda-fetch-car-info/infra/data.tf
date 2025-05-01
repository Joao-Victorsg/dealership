data "aws_caller_identity" "current"{

}

data "aws_ecr_repository" "lambda_fetch_car_info" {
  name = "joaovictorsg/lambda-fetch-car-info"
}

data "aws_apigatewayv2_api" "api_gateway_url"{
  api_id = "b6c8c8b9"
}