locals {
    lambda_fetch_car_info_arn = "${data.aws_lambda_function.fetch_car_info.arn}"
    lambda_fetch_client_info_arn = "${data.aws_lambda_function.fetch_client_info.arn}"
    lambda_invoice_processor_arn = "${data.aws_lambda_function.invoice_processor.arn}"
}