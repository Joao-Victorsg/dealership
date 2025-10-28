resource "aws_rds_cluster" "client_cluster" {
  cluster_identifier = "client-cluster"
  engine = "aurora-postgresql"
  database_name = "dealershipdb"
  master_username = data.aws_ssm_parameter.database_username.value
  master_password = data.aws_secretsmanager_secret_version.database_secret_version.secret_string
  skip_final_snapshot = true
  deletion_protection = true
  port = 4510
  vpc_security_group_ids = [aws_security_group.allow_traffic_database.id]
}

resource "aws_rds_cluster_instance" "client_instance"{
  cluster_identifier = aws_rds_cluster.client_cluster.id
  instance_class = "db.serverless"
  engine = aws_rds_cluster.client_cluster.engine
}

resource "aws_rds_cluster" "car_cluster" {
  cluster_identifier = "car-cluster"
  engine = "aurora-postgresql"
  database_name = "dealershipdb"
  master_username = data.aws_ssm_parameter.database_username.value
  master_password = data.aws_secretsmanager_secret_version.database_secret_version.secret_string
  skip_final_snapshot = true
  deletion_protection = true
  port = 4511
  vpc_security_group_ids = [aws_security_group.allow_traffic_database.id]
}

resource "aws_rds_cluster_instance" "car_instance"{
  cluster_identifier = aws_rds_cluster.car_cluster.id
  instance_class = "db.serverless"
  engine = aws_rds_cluster.car_cluster.engine
}

resource "aws_rds_cluster" "sales_cluster" {
  cluster_identifier = "sales-cluster"
  engine = "aurora-postgresql"
  database_name = "dealershipdb"
  master_username = data.aws_ssm_parameter.database_username.value
  master_password = data.aws_secretsmanager_secret_version.database_secret_version.secret_string
  skip_final_snapshot = true
  deletion_protection = true
  port = 4512
  vpc_security_group_ids = [aws_security_group.allow_traffic_database.id]
}

resource "aws_rds_cluster_instance" "sales_instance"{
  cluster_identifier = aws_rds_cluster.sales_cluster.id
  instance_class = "db.serverless"
  engine = aws_rds_cluster.sales_cluster.engine
}

resource "aws_rds_cluster" "keycloak_cluster" {
  cluster_identifier = "keycloak-cluster"
  engine = "aurora-postgresql"
  database_name = "keycloak-dealership"
  master_username = data.aws_ssm_parameter.database_username.value
  master_password = data.aws_secretsmanager_secret_version.database_secret_version.secret_string
  skip_final_snapshot = true
  deletion_protection = true
  port = 4513
  vpc_security_group_ids = [aws_security_group.allow_traffic_keycloak.id]
}

resource "aws_rds_cluster_instance" "keycloak_instance"{
  cluster_identifier = aws_rds_cluster.keycloak_cluster.id
  instance_class = "db.serverless"
  engine = aws_rds_cluster.keycloak_cluster.engine
}