data "aws_caller_identity" "current"{

}

data "aws_ecr_repository" "lambda_fetch_car_info" {
  name = "joaovictorsg/lambda-fetch-car-info"
}

data "aws_apigatewayv2_apis" "dealership_api_gateway" {
  name = "dealership-api-gateway"
}

data "aws_apigatewayv2_api" "api_gateway_url"{
  api_id = tolist(data.aws_apigatewayv2_apis.dealership_api_gateway.ids)[0]
}