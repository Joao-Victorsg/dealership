resource "aws_ssm_parameter" "database_username"{
  name = "/delearship/api/client/database/username"
  description = "Parameter that refers the username from the client database"
  type = "SecureString"
  value = var.client_database_username

  tags = {
    context = "delearship-client"
  }
}

resource "aws_ssm_parameter" "database_password" {
  name = "/dealership/api/client/database/password"
  description = "Parameter that contains the ARN of the secret that contains the database password"
  type = "String"
  value = aws_secretsmanager_secret.api_client_password.arn

  tags = {
    context = "delearship-client"
  }
}