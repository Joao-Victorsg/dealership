# API Gateway & VPC PrivateLink Setting

#1: API Gateway
resource "aws_apigatewayv2_api" "api" {
  name          = "dealership-api-gateway"
  protocol_type = "HTTP"
  body = file("${path.module}/open_api_specification/gateway_specification.yaml")

  tags = {
    "_custom_id_" = "ff50bf03"
  }
}

#2: VPC Link
resource "aws_apigatewayv2_vpc_link" "vpc_link" {
  name               = "dealership-vpclink"
  security_group_ids = [aws_security_group.security_group_lb.id]
  subnet_ids         = aws_subnet.private.*.id
}

#3: API Integration
resource "aws_apigatewayv2_integration" "clients_integration" {
  api_id             = aws_apigatewayv2_api.api.id
  integration_type   = "HTTP_PROXY"
  connection_id      = aws_apigatewayv2_vpc_link.vpc_link.id
  connection_type    = "VPC_LINK"
  description        = "Clients API integration"
  integration_method = "ANY"
  integration_uri    = "${aws_lb.dealership_lb.dns_name}:8085"

  depends_on         = [aws_lb.dealership_lb]
}

resource "aws_apigatewayv2_integration" "cars_integration" {
  api_id             = aws_apigatewayv2_api.api.id
  integration_type   = "HTTP_PROXY"
  connection_id      = aws_apigatewayv2_vpc_link.vpc_link.id
  connection_type    = "VPC_LINK"
  description        = "Cars API integration"
  integration_method = "ANY"
  integration_uri    = "http://${aws_lb.dealership_lb.dns_name}:8087"
  #integration_uri    = "http://host.docker.internal:8087"


  depends_on         = [aws_lb.dealership_lb]
}

resource "aws_apigatewayv2_integration" "sales_integration" {
  api_id             = aws_apigatewayv2_api.api.id
  integration_type   = "HTTP_PROXY"
  connection_id      = aws_apigatewayv2_vpc_link.vpc_link.id
  connection_type    = "VPC_LINK"
  description        = "Sales API integration"
  integration_method = "ANY"
  integration_uri    = "${aws_lb.dealership_lb.dns_name}:8086"

  depends_on         = [aws_lb.dealership_lb]
}

#4: APIGW Route
resource "aws_apigatewayv2_route" "clients_routes" {
  api_id    = aws_apigatewayv2_api.api.id
  route_key = "ANY /v1/dealership/clients/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.clients_integration.id}"
}

resource "aws_apigatewayv2_route" "cars_routes" {
  api_id    = aws_apigatewayv2_api.api.id
  route_key = "ANY /v1/dealership/cars/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.cars_integration.id}"
}

resource "aws_apigatewayv2_route" "sales_routes" {
  api_id    = aws_apigatewayv2_api.api.id
  route_key = "ANY /v1/dealership/sales/{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.sales_integration.id}"
}

#5: APIGW Stage
resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.api.id
  name        = "$default"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.api_gw_log_group.arn
    format          = jsonencode({
      requestId       = "$context.requestId"
      ip              = "$context.identity.sourceIp"
      caller          = "$context.identity.caller"
      user            = "$context.identity.user"
      requestTime     = "$context.requestTime"
      httpMethod      = "$context.httpMethod"
      resourcePath    = "$context.resourcePath"
      status          = "$context.status"
      protocol        = "$context.protocol"
      responseLength  = "$context.responseLength"
    })
  }
}

resource "aws_cloudwatch_log_group" "api_gw_log_group" {
  name              = "/aws/api-gateway/dealership-api"
  retention_in_days = 14
}