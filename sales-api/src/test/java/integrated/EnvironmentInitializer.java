package integrated;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;

import java.util.Map;

import static integrated.client.SnsContainerClient.createSnsTopic;
import static integrated.client.SnsContainerClient.getTopicArn;
import static integrated.client.SnsContainerClient.subscribeQueue;
import static integrated.client.SqsContainerClient.createQueue;
import static integrated.container.LocalStackContainerDefinition.getLocalstackUrl;
import static integrated.container.LocalStackContainerDefinition.startLocalstackContainer;
import static integrated.container.PostgresContainerDefinition.getPostgresUrl;
import static integrated.container.PostgresContainerDefinition.startPostgresContainer;

public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final Network NETWORK = Network.newNetwork();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startPostgresContainer();
        startLocalstackContainer();
        createSnsTopic("sales-topic");
        createQueue();
        subscribeQueue();

        final var properties = Map.of(
                "spring.datasource.url",getPostgresUrl(),
                "spring.cloud.aws.sns.endpoint", getLocalstackUrl(),
                "sales.sns.topic.arn", getTopicArn()
        );

        TestPropertyValues.of(properties).applyTo(applicationContext);
    }

}