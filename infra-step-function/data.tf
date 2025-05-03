data "aws_lambda_function" "invoice_processor" {
  function_name = "InvoiceProcessor"
}

data "aws_lambda_function" "fetch_car_info" {
    function_name = "Fetch-Car-Info"
}

data "aws_lambda_function" "fetch_client_info" {
    function_name = "fetch-client-Info"
}