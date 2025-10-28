resource "aws_security_group" "allow_traffic_database"{
  name = "Allow traffic database"
  description = "Allowing all the traffic to database to test"
  vpc_id = data.aws_vpc.vpc.id
}

resource "aws_vpc_security_group_ingress_rule" "allow_all_trafic_test" {
  security_group_id = aws_security_group.allow_traffic_database.id
  cidr_ipv4 = "0.0.0.0/0"
  ip_protocol = "-1"
}

# Keycloak Database Security Group - Dedicated security group for Keycloak database access
resource "aws_security_group" "allow_traffic_keycloak"{
  name = "Allow traffic keycloak database"
  description = "Security group for Keycloak database access"
  vpc_id = data.aws_vpc.vpc.id
}

resource "aws_vpc_security_group_ingress_rule" "allow_keycloak_database_traffic" {
  security_group_id = aws_security_group.allow_traffic_keycloak.id
  cidr_ipv4 = "0.0.0.0/0"
  ip_protocol = "-1"
}