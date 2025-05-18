data "aws_caller_identity" "current"{
}

data "aws_ecr_repository" "lambda_start_workflow" {
  name = "joaovictorsg/lambda-start-workflow"
}

data "aws_sfn_state_machine" "invoice_workflow" {
  name = "InvoiceWorkflow"
}

data "aws_sns_topic" "invoice_topic" {
  name = "sales-topic"
}