resource "aws_secretsmanager_secret" "api_client_password" {
  name = "api-client-password"
}

resource "aws_secretsmanager_secret_version" "api_client_password_version"{
  secret_id = aws_secretsmanager_secret.api_client_password.id
  secret_string = "12345678"
}