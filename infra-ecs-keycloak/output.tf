output "lb_arn" {
  value = data.aws_lb.api_dealership.arn
}

output "subnets" {
  value = data.aws_subnets.subnets
}

output "keycloak_service_url" {
  value = "http://${data.aws_lb.api_dealership.dns_name}:8080"
}