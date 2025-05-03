data "template_file" "state_machine_definition" {
  template = file("${path.module}/workflow-definition.json")

  vars = {
    lambda_fetch_car_info_arn    = local.lambda_fetch_car_info_arn
    lambda_fetch_client_info_arn = local.lambda_fetch_client_info_arn
    lambda_invoice_processor_arn = local.lambda_invoice_processor_arn
  }
}


resource "aws_sfn_state_machine" "invoice_workflow" {
  name     = "InvoiceWorkflow"
  role_arn = aws_iam_role.step_function_role.arn
  definition = data.template_file.state_machine_definition.rendered
  type     = "STANDARD"
}
