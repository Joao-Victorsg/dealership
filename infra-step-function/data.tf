data "aws_lambda_function" "invoice_processor" {
  function_name = "Invoice-Processor"
}

data "aws_lambda_function" "fetch_car_info" {
    function_name = "Fetch-Car-Info"
}

data "aws_lambda_function" "fetch_client_info" {
    function_name = "fetch-client-Info"
}

data "aws_lambda_function" "send_mail" {
    function_name = "send-mail"
}