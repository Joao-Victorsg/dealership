package integrated.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static integrated.EnvironmentInitializer.NETWORK;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

public class LocalStackContainerDefinition {

    private static final String LOCALSTACK_URL = "http://%s:%d";

    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("localstack/localstack:stable");

    private static final GenericContainer<?> LOCALSTACK_CONTAINER = new LocalStackContainer(DOCKER_IMAGE_NAME)
            .withExposedPorts(4566)
            .withServices(SQS)
            .withNetwork(NETWORK);

    public static void startLocalstackContainer(){
        LOCALSTACK_CONTAINER.start();
    }

    public static String getLocalstackUrl(){
        return String.format(LOCALSTACK_URL,
                LOCALSTACK_CONTAINER.getHost(),LOCALSTACK_CONTAINER.getFirstMappedPort());
    }

}