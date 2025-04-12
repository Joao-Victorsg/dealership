resource "aws_sqs_queue" "creation_events_queue"{
  name = "client-car-creation-event-queue"
  message_retention_seconds = 1209600 #14 days retention

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.creation_events_dlq.arn
    maxReceiveCount = 5
  })
}

resource "aws_sqs_queue" "creation_events_dlq"{
  name = "client-car-creation-event-dlq"
  message_retention_seconds = 1209600 #14 days retention
}