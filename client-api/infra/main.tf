#ECS Cluster & Fargate Setting
resource "aws_ecs_task_definition" "api-client" {
  family                   = "api-client"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048

  container_definitions = <<DEFINITION
[
  {
    "image": "joaovictorsg/api-client:v1.2",
    "cpu": 1024,
    "memory": 2048,
    "name": "client-api",
    "networkMode": "awsvpc",
    "portMappings": [
      {
        "containerPort": 8085,
        "hostPort": 8085
      }
    ],
    "environment": [
      {
        "name": "SPRING_PROFILES_ACTIVE",
        "value": "container"
      }
    ]
  }
]
DEFINITION
}

resource "aws_security_group" "client_api_sg" {
  name   = "api-client-task-sg"
  vpc_id = data.aws_vpc.vpc.id

  ingress {
    protocol        = "tcp"
    from_port       = 8085
    to_port         = 8085
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb_target_group" "client-api" {
  name        = "api-client-target-group"
  port        = 8085
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

resource "aws_lb_listener_rule" "client_rule"{
  listener_arn = data.aws_lb_listener.lb_listener.arn
  priority = 10

  condition {
    path_pattern {
      values = ["/v1/dealership/clients*"]
    }
  }

  action {
    type = "forward"
    target_group_arn = aws_lb_target_group.client-api.arn
  }
}

resource "aws_ecs_service" "client_api" {
  name            = "client-api-service"
  cluster         = data.aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.api-client.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.client_api_sg.id]
    subnets = data.aws_subnets.subnets.ids
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.client-api.id
    container_name   = "client-api"
    container_port   = 8085
  }
}

