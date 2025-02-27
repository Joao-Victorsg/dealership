package integrated.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static integrated.EnvironmentInitializer.NETWORK;

public class WiremockContainerDefinition {

    private static final DockerImageName WIREMOCK_IMAGE = DockerImageName
            .parse("wiremock/wiremock:latest");

    private static final String WIREMOCK_URL = "http://%s:%d";

    private static final GenericContainer<?> WIREMOCK_CONTAINER =  new GenericContainer<>(WIREMOCK_IMAGE)
                .withNetwork(NETWORK)
                .withNetworkAliases("wiremock")
                .withExposedPorts(8080);

    public static void startWiremockContainer(){
        WIREMOCK_CONTAINER.start();
    }

    public static String getWiremockUrl() {
        return String.format(WIREMOCK_URL,
                WIREMOCK_CONTAINER.getHost(),WIREMOCK_CONTAINER.getFirstMappedPort());
    }

    public static String getWiremockHost(){
        return WIREMOCK_CONTAINER.getHost();
    }

    public static Integer getWiremockPort(){
        return WIREMOCK_CONTAINER.getFirstMappedPort();
    }
}