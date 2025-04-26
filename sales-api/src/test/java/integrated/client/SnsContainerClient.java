package integrated.client;

import integrated.container.LocalStackContainerDefinition;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

import java.net.URI;

public class SnsContainerClient {

    private static final SnsClient snsClient = SnsClient.builder()
            .endpointOverride(URI.create(LocalStackContainerDefinition.getLocalstackUrl()))
            .region(Region.US_EAST_1)
            .build();

    private static String TOPIC_ARN;


    public static void createSnsTopic(String topicName){
        final var createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        TOPIC_ARN = snsClient.createTopic(createTopicRequest).topicArn();
    }

    public static String getTopicArn(){
        return TOPIC_ARN;
    }

    public static void subscribeQueue(){
        final var subscribeRequest = SubscribeRequest.builder()
                .topicArn(TOPIC_ARN)
                .protocol("sqs")
                .endpoint(SqsContainerClient.getQueueArn())
                .build();

        snsClient.subscribe(subscribeRequest);
    }
}
