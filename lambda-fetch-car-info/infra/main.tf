resource "aws_lambda_function" "fetch_car_info" {
  function_name = "Fetch-Car-Info"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_fetch_car_info.repository_url}:latest"
  package_type = "Image"

  environment {
    variables = {
      #CAR_API_URL = "${data.aws_apigatewayv2_api.api_gateway_url.api_endpoint}/v1/dealership/cars"
      CAR_API_URL = "http://host.docker.internal:8087/v1/dealership/cars"
    }
  }
}