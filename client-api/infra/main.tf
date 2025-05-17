#ECS Cluster & Fargate Setting
resource "aws_ecs_task_definition" "api-client" {
  family       = "api-client"
  network_mode = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu          = 1024
  memory       = 2048

  container_definitions = <<DEFINITION
[
  {
    "image": "joaovictorsg/client-api:1.3",
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
    protocol  = "tcp"
    from_port = 8085
    to_port   = 8085
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  egress {
    protocol  = "-1"
    from_port = 0
    to_port   = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb_target_group" "client_api" {
  name        = "api-client-target-group"
  port        = 8085
  protocol    = "TCP"
  vpc_id      = data.aws_vpc.vpc.id
  target_type = "ip"

  health_check {
    enabled             = true
    protocol            = "HTTP"
    port                = 8085
    path                = "/actuator/health"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 6
  }
}

resource "aws_lb_listener" "client-api" {
  load_balancer_arn = data.aws_lb.lb.arn
  port              = "8085"
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.client_api.arn
  }

  depends_on = [aws_lb_target_group.client_api]
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
    target_group_arn = aws_lb_target_group.client_api.id
    container_name   = "client-api"
    container_port   = 8085
  }
}

