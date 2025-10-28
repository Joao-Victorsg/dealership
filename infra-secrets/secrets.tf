resource "aws_secretsmanager_secret" "database_password" {
  name = "database-password"
}

resource "aws_secretsmanager_secret_version" "database_password_version"{
  secret_id = aws_secretsmanager_secret.database_password.id
  secret_string = "12345678"
}

resource "aws_secretsmanager_secret" "keycloak_admin_password" {
  name = "keycloak-admin-password"
}

resource "aws_secretsmanager_secret_version" "keycloak_admin_password_version"{
  secret_id = aws_secretsmanager_secret.keycloak_admin_password.id
  secret_string = "admin123"
}