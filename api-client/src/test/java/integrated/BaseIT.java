package integrated;

import br.com.dealership.api_client.ApiClientApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(classes = ApiClientApplication.class, webEnvironment = DEFINED_PORT)
@ActiveProfiles("it")
@ContextConfiguration(initializers = EnvironmentInitializer.class)
@EnableFeignClients
public class BaseIT {

    @BeforeAll
    static void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}