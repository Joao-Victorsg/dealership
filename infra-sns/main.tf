resource "aws_sns_topic" "sales_topic" {
  name = "sales-topic"
}

resource "aws_sqs_queue" "sales_dlq" {
  name = "sales-dlq"
}

resource "aws_sns_topic_subscription" "sales_topic_dlq" {
  topic_arn = aws_sns_topic.sales_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.sales_dlq.arn
  raw_message_delivery = true
}