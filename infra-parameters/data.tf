data "aws_secretsmanager_secret" "database_secret"{
  name = "database-password"
}