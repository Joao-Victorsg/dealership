output "lb_arn" {
  value = data.aws_lb.lb.arn
}

output "subnets" {
  value = data.aws_subnets.subnets
}