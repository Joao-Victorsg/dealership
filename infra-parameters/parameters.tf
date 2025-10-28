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

# Keycloak Admin Parameters
resource "aws_ssm_parameter" "keycloak_admin_username" {
  name = "/dealership/keycloak/admin/username"
  description = "Keycloak admin username"
  type = "String"
  value = var.keycloak_admin_username

  tags = {
    context = "dealership-keycloak"
  }
}

resource "aws_ssm_parameter" "keycloak_admin_password" {
  name = "/dealership/keycloak/admin/password"
  description = "Parameter that contains the ARN of the secret that contains the Keycloak admin password"
  type = "String"
  value = data.aws_secretsmanager_secret.keycloak_admin_password.arn

  tags = {
    context = "dealership-keycloak"
  }
}