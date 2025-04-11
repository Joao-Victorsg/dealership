resource "aws_sns_topic" "event_creations" {
  name = "car_client_creation_event"
}