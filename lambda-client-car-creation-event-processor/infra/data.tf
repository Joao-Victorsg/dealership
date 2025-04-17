data "aws_caller_identity" "current"{

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

data "aws_sqs_queue" "creation_event_queue"{
  name = "client-car-creation-event-queue"
}