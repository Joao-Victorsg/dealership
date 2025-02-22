#AWS ALB Setting
resource "aws_lb" "default" {
  name            = "api-dealership"
  subnets         = aws_subnet.public.*.id
  security_groups = [aws_security_group.security_group_lb.id]
}

resource "aws_lb_target_group" "dealership-api" {
  name        = "api-dealership-target-group"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.vpc_dealership.id
  target_type = "ip"
}

resource "aws_lb_listener" "dealership-api" {
  load_balancer_arn = aws_lb.default.id
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_lb_target_group.dealership-api.id
    type             = "forward"
  }
}