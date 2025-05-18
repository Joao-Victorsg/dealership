#AWS ALB Setting
resource "aws_lb" "dealership_lb" {
  name            = "api-dealership"
  subnets         = aws_subnet.private.*.id
  load_balancer_type = "network"
  internal = true
  tags = {
    "Name" = "api-dealership"
  }
}