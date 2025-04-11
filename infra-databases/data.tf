data "aws_vpc" "vpc"{
  filter {
    name = "tag:Name"
    values = ["vpc_dealership"]
  }
}

data "aws_ssm_parameter" "database_username"{
  name = "/delearship/api/database/username"
}

data "aws_ssm_parameter" "database_password"{
  name = "/dealership/api/database/password"
}

data "aws_secretsmanager_secret" "database_secret"{
  arn = data.aws_ssm_parameter.database_password.value
}

data "aws_secretsmanager_secret_version" "database_secret_version"{
  secret_id = data.aws_secretsmanager_secret.database_secret.id
}