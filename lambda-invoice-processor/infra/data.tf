data "aws_caller_identity" "current"{

}

data "aws_ecr_repository" "lambda_invoice_processor" {
  name = "joaovictorsg/lambda-invoice-processor"
}