data "aws_secretsmanager_secret" "database_secret"{
  name = "database-password"
}

data "aws_secretsmanager_secret" "keycloak_admin_password"{
  name = "keycloak-admin-password"
}