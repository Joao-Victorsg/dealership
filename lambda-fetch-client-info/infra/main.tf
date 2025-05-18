resource "aws_lambda_function" "fetch_client_info" {
  function_name = "fetch-client-Info"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_fetch_client_info.repository_url}:latest"
  package_type = "Image"
  timeout = 30

  environment {
    variables = {
      #CLIENT_API_URL = "${data.aws_apigatewayv2_api.api_gateway_url.api_endpoint}/v1/dealership/clients"
      CLIENT_API_URL = "http://host.docker.internal:8085/v1/dealership/clients"
    }
  }
}