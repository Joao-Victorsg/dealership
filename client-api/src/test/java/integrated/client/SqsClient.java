package integrated.client;

import integrated.container.LocalStackContainerDefinition;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.messaging.Message;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;
import java.util.Optional;

public class SqsClient {

    private static final SqsTemplate sqsTemplate = SqsTemplate.builder()
            .sqsAsyncClient(SqsAsyncClient.builder()
                    .endpointOverride(URI.create(LocalStackContainerDefinition.getLocalstackUrl()))
                    .region(Region.US_EAST_1)
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build())
            .build();

    public static Optional<Message<String>> getMessageFromQueue(String queueName){
        return sqsTemplate.receive(queueName,String.class);
    }
}