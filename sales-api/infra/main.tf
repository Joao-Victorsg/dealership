#ECS Cluster & Fargate Setting
resource "aws_ecs_task_definition" "sales_api" {
  family                   = "sales-api"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048

  container_definitions = <<DEFINITION
[
  {
    "image": "joaovictorsg/sales-api:1.0",
    "cpu": 1024,
    "memory": 2048,
    "name": "sales-api",
    "networkMode": "awsvpc",
    "portMappings": [
      {
        "containerPort": 8086,
        "hostPort": 8086
      }
    ],
    "environment": [
    ]
  }
]
DEFINITION
}

resource "aws_security_group" "sales_api_sg" {
  name   = "sales-api-task-sg"
  vpc_id = data.aws_vpc.vpc.id

  ingress {
    protocol        = "tcp"
    from_port       = 8086
    to_port         = 8086
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb_target_group" "sales_api" {
  name        = "sales-api-target-group"
  port        = 8086
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.vpc.id
  target_type = "ip"

  health_check {
    path                = "/actuator/health"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

resource "aws_lb_listener_rule" "sales_rule"{
  listener_arn = data.aws_lb_listener.lb_listener.arn
  priority = 11

  condition {
    path_pattern {
      values = ["/v1/dealership/saless*"]
    }
  }

  action {
    type = "forward"
    target_group_arn = aws_lb_target_group.sales_api.arn
  }
}

resource "aws_ecs_service" "sales_api" {
  name            = "sales-api"
  cluster         = data.aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.sales_api.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.sales_api_sg.id]
    subnets = data.aws_subnets.subnets.ids
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.sales_api.id
    container_name   = "sales-api"
    container_port   = 8086
  }
}

