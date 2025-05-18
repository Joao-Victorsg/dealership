data "aws_caller_identity" "current"{

}

data "aws_ecr_repository" "lambda_send_mail" {
  name = "joaovictorsg/lambda-send-mail"
}