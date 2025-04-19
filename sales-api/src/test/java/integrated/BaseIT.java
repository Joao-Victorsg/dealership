package integrated;

import br.com.dealership.sales_api.SalesApiApplication;
import io.restassured.RestAssured;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SpringBootTest(classes = SalesApiApplication.class, webEnvironment = DEFINED_PORT)
@ActiveProfiles("it")
@ContextConfiguration(initializers = EnvironmentInitializer.class)
public class BaseIT {

    @BeforeAll
    static void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}