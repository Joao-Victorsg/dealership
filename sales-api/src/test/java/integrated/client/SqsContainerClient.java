package integrated.client;

import integrated.container.LocalStackContainerDefinition;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.net.URI;
import java.util.List;

public class SqsContainerClient {

    private static String QUEUE_URL;

    private static final String QUEUE_NAME = "sales-queue-it";

    private static final SqsClient sqsClient = SqsClient.builder()
            .endpointOverride(URI.create(LocalStackContainerDefinition.getLocalstackUrl()))
            .region(Region.US_EAST_1)
            .build();

    public static void createQueue(){
        final var createQueueRequest = CreateQueueRequest.builder()
                .queueName(QUEUE_NAME)
                .build();

        QUEUE_URL = sqsClient.createQueue(createQueueRequest).queueUrl();
    }

    public static String getQueueArn(){
        final var queue = GetQueueAttributesRequest.builder()
                .queueUrl(QUEUE_URL)
                .attributeNames(QueueAttributeName.QUEUE_ARN)
                .build();

        return sqsClient.getQueueAttributes(queue).attributes().get(QueueAttributeName.QUEUE_ARN);
    }

    public static List<Message> getMessageFromQueue(){
        final var receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .build();
        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }
}
