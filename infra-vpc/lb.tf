#AWS ALB Setting
resource "aws_lb" "default" {
  name            = "api-dealership"
  subnets         = aws_subnet.private.*.id
  security_groups = [aws_security_group.security_group_lb.id]
  internal = true
}

resource "aws_lb_listener" "dealership-api" {
  load_balancer_arn = aws_lb.default.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "404 Not Found"
      status_code  = "404"
    }
  }
}