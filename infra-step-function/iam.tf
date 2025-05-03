resource "aws_iam_role" "step_function_role" {
  name = "step-function-invoice-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "states.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy" "step_function_policy" {
  name = "step-function-lambda-policy"
  role = aws_iam_role.step_function_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "lambda:InvokeFunction"
        ]
        Resource = [
          local.lambda_fetch_car_info_arn,
          local.lambda_fetch_client_info_arn,
          local.lambda_invoice_processor_arn
        ]
      }
    ]
  })
}
