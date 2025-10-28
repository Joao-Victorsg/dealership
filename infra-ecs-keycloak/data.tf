data "aws_vpc" "vpc"{
  filter {
    name = "tag:Name"
    values = ["vpc_dealership"]
  }
}

data "aws_security_group" "lb_sg"{
  filter {
    name = "tag:Name"
    values = ["lb_security_group"]
  }
}

data "aws_subnets" "subnets"{
  filter {
    name = "tag:Name"
    values = ["aws_subnet_private"]
  }
}

data "aws_lb" "api_dealership"{
  name = "api-dealership"
}

# Keycloak specific parameters
data "aws_ssm_parameter" "keycloak_admin_username" {
  name = "/dealership/keycloak/admin/username"
}

data "aws_ssm_parameter" "keycloak_admin_password" {
  name = "/dealership/keycloak/admin/password"
}

data "aws_secretsmanager_secret" "keycloak_admin_secret"{
  arn = data.aws_ssm_parameter.keycloak_admin_password.value
}

data "aws_secretsmanager_secret_version" "keycloak_admin_secret_version"{
  secret_id = data.aws_secretsmanager_secret.keycloak_admin_secret.id
}


# Database parameters (using same pattern as other databases for study project)
data "aws_ssm_parameter" "database_username" {
  name = "/delearship/api/database/username"
}

data "aws_ssm_parameter" "database_password" {
  name = "/dealership/api/database/password"
}

data "aws_secretsmanager_secret" "database_secret"{
  arn = data.aws_ssm_parameter.database_password.value
}

data "aws_secretsmanager_secret_version" "database_secret_version"{
  secret_id = data.aws_secretsmanager_secret.database_secret.id
}

# Keycloak RDS cluster
data "aws_rds_cluster" "keycloak_cluster" {
  cluster_identifier = "keycloak-cluster"
} 

data "aws_ecs_cluster" "main" {
  cluster_name = "api-dealership"
}