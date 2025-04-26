#ECS Cluster & Fargate Setting
resource "aws_ecs_task_definition" "car_api" {
  family                   = "car-api"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048

  container_definitions = <<DEFINITION
[
  {
    "image": "joaovictorsg/car-api:1.4",
    "cpu": 1024,
    "memory": 2048,
    "name": "car-api",
    "networkMode": "awsvpc",
    "portMappings": [
      {
        "containerPort": 8087,
        "hostPort": 8087
      }
    ],
    "environment": [
    ]
  }
]
DEFINITION
}

resource "aws_security_group" "car_api_sg" {
  name   = "car-api-task-sg"
  vpc_id = data.aws_vpc.vpc.id

  ingress {
    protocol        = "tcp"
    from_port       = 8087
    to_port         = 8087
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb_target_group" "car_api" {
  name        = "car-api-target-group"
  port        = 8087
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

resource "aws_lb_listener_rule" "car_rule"{
  listener_arn = data.aws_lb_listener.lb_listener.arn
  priority = 11

  condition {
    path_pattern {
      values = ["/v1/dealership/cars*"]
    }
  }

  action {
    type = "forward"
    target_group_arn = aws_lb_target_group.car_api.arn
  }
}

resource "aws_ecs_service" "car_api" {
  name            = "car-api"
  cluster         = data.aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.car_api.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.car_api_sg.id]
    subnets = data.aws_subnets.subnets.ids
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.car_api.id
    container_name   = "car-api"
    container_port   = 8087
  }
}

