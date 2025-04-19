package integrated.container;

import lombok.NoArgsConstructor;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import static integrated.EnvironmentInitializer.NETWORK;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PostgresContainerDefinition {
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName
            .parse("postgres:latest");

    private static final String POSTGRES_URL = "jdbc:postgresql://%s:%d/dealershipdb";

    private static final GenericContainer<?> POSTGRES_CONTAINER = new GenericContainer<>(POSTGRES_IMAGE)
                .withNetwork(NETWORK)
                .withNetworkAliases("postgres")
                .withEnv("POSTGRES_USER","dealership")
                .withEnv("POSTGRES_PASSWORD","12345678")
                .withEnv("POSTGRES_DB","dealershipdb")
                .withExposedPorts(5432)
                .withCopyFileToContainer(MountableFile.forHostPath("src/test/resources/scripts/init.sql"), "/docker-entrypoint-initdb.d/init.sql")
                .waitingFor(Wait.forListeningPort());

    public static void startPostgresContainer(){
        POSTGRES_CONTAINER.start();
    }

    public static String getPostgresUrl() {
        return String.format(POSTGRES_URL,
                POSTGRES_CONTAINER.getHost(),POSTGRES_CONTAINER.getFirstMappedPort());
    }

}