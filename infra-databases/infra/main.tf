resource "aws_rds_cluster" "postgresql" {
  cluster_identifier = "dealership-cluster"
  engine = "aurora-postgresql"
  database_name = "dealershipdb"
  master_username = "dealership"
  master_password = 12345678
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