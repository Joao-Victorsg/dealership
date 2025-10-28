# Keycloak ECS Infrastructure
# This module deploys Keycloak on AWS ECS Fargate for production use


# ECS Task Definition
resource "aws_ecs_task_definition" "keycloak" {
  family                   = "keycloak"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048

  container_definitions = <<DEFINITION
[
  {
    "image": "quay.io/keycloak/keycloak:latest",
    "cpu": 1024,
    "memory": 2048,
    "name": "keycloak",
    "networkMode": "awsvpc",
    "portMappings": [
      {
        "containerPort": 8084,
        "hostPort": 8084
      },
      {
        "containerPort":9000,
        "hostPort": 9000
      }
    ],
    "environment": [
      {
        "name": "KC_BOOTSTRAP_ADMIN_USERNAME",
        "value": "${data.aws_ssm_parameter.keycloak_admin_username.value}"
      },
      {
        "name": "KC_DB",
        "value": "postgres"
      },
      {
        "name": "KC_DB_URL",
        "value": "jdbc:postgresql://${data.aws_rds_cluster.keycloak_cluster.endpoint}:${data.aws_rds_cluster.keycloak_cluster.port}/${data.aws_rds_cluster.keycloak_cluster.database_name}"
      },
      {
        "name": "KC_DB_USERNAME",
        "value": "${data.aws_ssm_parameter.database_username.value}"
      },
      {
        "name": "KC_HOSTNAME_STRICT",
        "value": "false"
      },
      {
        "name": "KC_HOSTNAME_STRICT_HTTPS",
        "value": "false"
      },
      {
        "name": "KC_HTTP_ENABLED",
        "value": "true"
      },
      {
        "name": "KC_HEALTH_ENABLED",
        "value": "true"
      },
      {
        "name": "KC_METRICS_ENABLED",
        "value": "false"
      },
      {
        "name": "KC_HTTPS_PORT",
        "value": "8084"
      },
      {
        "name": "KC_HTTP_PORT",
        "value": "8084"
      },
      {
        "name": "KC_CACHE",
        "value": "local"
      }
    ],
    "secrets": [
      {
        "name": "KC_BOOTSTRAP_ADMIN_PASSWORD",
        "valueFrom": "${data.aws_secretsmanager_secret_version.keycloak_admin_secret_version.arn}"
      },
      {
        "name": "KC_DB_PASSWORD",
        "valueFrom": "${data.aws_secretsmanager_secret_version.database_secret_version.arn}"
      }
    ],
    "command": ["start-dev"]
  }
]
DEFINITION
}

# ECS Service
resource "aws_ecs_service" "keycloak" {
  name            = "keycloak"
  cluster         = data.aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.keycloak.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.keycloak_sg.id]
    subnets = data.aws_subnets.subnets.ids
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.keycloak.id
    container_name   = "keycloak"
    container_port   = 8084
  }
}

# Security Group for Keycloak ECS Task
resource "aws_security_group" "keycloak_sg" {
  name   = "keycloak-task-sg"
  vpc_id = data.aws_vpc.vpc.id

  ingress {
    protocol        = "tcp"
    from_port       = 8084
    to_port         = 8084
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  ingress {
    protocol        = "tcp"
    from_port       = 9000
    to_port         = 9000
    security_groups = [data.aws_security_group.lb_sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "keycloak-task-sg"
  }
}

# ALB Target Group
resource "aws_lb_target_group" "keycloak" {
  name        = "keycloak-target-group"
  port        = 8084
  protocol    = "TCP"
  vpc_id      = data.aws_vpc.vpc.id
  target_type = "ip"

  health_check {
    enabled             = true
    port                = 9000
    path                = "/health"
    protocol            = "HTTP"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 6
  }
}

# ALB Listener
resource "aws_lb_listener" "keycloak" {
  load_balancer_arn = data.aws_lb.api_dealership.arn
  port              = "8084"
  protocol          = "TCP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.keycloak.arn
  }

  depends_on = [aws_lb_target_group.keycloak]
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "keycloak_logs" {
  name              = "/ecs/keycloak"
  retention_in_days = 30

  tags = {
    Name        = "keycloak-logs"
    Environment = "production-teste"
    Project     = "dealership"
  }
}