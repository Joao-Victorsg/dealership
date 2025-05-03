resource "aws_lambda_function" "fetch_car_info" {
  function_name = "Fetch-Car-Info"
  role          = aws_iam_role.lambda_exec_role.arn
  image_uri = "${data.aws_ecr_repository.lambda_fetch_car_info.repository_url}:2.0"
  package_type = "Image"

  environment {
    variables = {
      CAR_API_URL = "http://b6c8c8b9.execute-api.localhost.localstack.cloud:8087/v1/dealership/cars"
    }
  }
}