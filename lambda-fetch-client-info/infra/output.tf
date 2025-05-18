output "teste" {
  value = tolist(data.aws_apigatewayv2_apis.dealership_api_gateway.ids)[0]
}

output "endpoint"{
    value = data.aws_apigatewayv2_api.api_gateway_url.api_endpoint
}