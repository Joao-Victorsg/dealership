resource "aws_ssm_parameter" "database_username"{
  name = "/delearship/api/database/username"
  description = "Parameter that refers the username from the database"
  type = "SecureString"
  value = var.database_username

  tags = {
    context = "delearship-client-api"
  }
}

resource "aws_ssm_parameter" "database_password" {
  name = "/dealership/api/database/password"
  description = "Parameter that contains the ARN of the secret that contains the database password"
  type = "String"
  value = data.aws_secretsmanager_secret.database_secret.arn

  tags = {
    context = "delearship-client-api"
  }
}