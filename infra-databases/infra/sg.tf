resource "aws_security_group" "allow_traffic_database"{
  name = "Allow traffic database"
  description = "Allowing all the traffic to database to test"
  vpc_id = "vpc-b3bec064"
}

resource "aws_vpc_security_group_ingress_rule" "allow_all_trafic_test" {
  security_group_id = aws_security_group.allow_traffic_database.id
  cidr_ipv4 = "0.0.0.0/0"
  ip_protocol = "-1"
}