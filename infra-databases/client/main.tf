resource "aws_rds_cluster" "postgresql" {
  cluster_identifier = "client-cluster"
  engine = "aurora-postgresql"
  database_name = var.client_database_name
  master_username = var.client_database_username
  master_password = "${aws_secretsmanager_secret_version.api_client_password_version.secret_string}"
  skip_final_snapshot = true
  deletion_protection = true
  port = 4566
  vpc_security_group_ids = [aws_security_group.allow_traffic_database.id]
}

resource "aws_rds_cluster_instance" "delership_instance"{
  cluster_identifier = aws_rds_cluster.postgresql.id
  instance_class = "db.serverless"
  engine = aws_rds_cluster.postgresql.engine
}