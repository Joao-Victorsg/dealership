package integrated;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;

import java.util.Map;


import static integrated.container.PostgresContainerDefinition.getPostgresUrl;
import static integrated.container.PostgresContainerDefinition.startPostgresContainer;

public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final Network NETWORK = Network.newNetwork();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startPostgresContainer();

        final var properties = Map.of(
                "spring.datasource.url",getPostgresUrl()
        );

        TestPropertyValues.of(properties).applyTo(applicationContext);
    }

}