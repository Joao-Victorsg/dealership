data "aws_vpc" "vpc"{
  filter {
    name = "tag:Name"
    values = ["vpc_dealership"]
  }
}

data "aws_security_group" "lb_sg"{
  filter {
    name = "tag:Name"
    values = ["lb_security_group"]
  }
}

data "aws_subnets" "subnets"{
  filter {
    name = "tag:Name"
    values = ["aws_subnet_private"]
  }
}

data "aws_lb" "lb"{
  name = "api-dealership"
}

data "aws_lb_listener" "lb_listener" {
  load_balancer_arn = data.aws_lb.lb.arn
  port = 80
}

data "aws_ecs_cluster" "cluster"{
  cluster_name = "api-dealership"
}

data "aws_sns_topic" "sales_topic"{
  name = "sales-topic"
}