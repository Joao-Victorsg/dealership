resource "aws_lambda_function" "fetch_client_info" {
  function_name = "fetch-client-Info"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_fetch_client_info.repository_url}:1.0"
  package_type = "Image"

  environment {
    variables = {
      client_API_URL = "${data.aws_apigatewayv2_api.api_gateway_url.api_endpoint}:8085/v1/dealership/clients"
    }
  }
}