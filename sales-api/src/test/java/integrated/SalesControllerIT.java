package integrated;

import br.com.dealership.sales_api.adapter.in.controller.dto.request.SalesDtoRequest;
import integrated.client.SqsContainerClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@RequiredArgsConstructor
 class SalesControllerIT extends BaseIT{

    private static final String URL_WITH_ID_PATH_PARAMETER = "/v1/dealership/sales/{id}";
    private static final String URL = "/v1/dealership/sales";

    @DisplayName("Given a valid request to create a sale then create it")
    @Test
     void givenValidRequestToCreateASaleThenCreateIt(){
       final var sale = createSalesDtoRequest("12345678900","1HGCM82633A123456");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(sale)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.id", notNullValue())
                .body("data.cpf", equalTo(sale.cpf()))
                .body("data.vin", equalTo(sale.vin()));

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    final var message = SqsContainerClient.getMessageFromQueue();
                    assertNotNull(message);
                    assertFalse(message.isEmpty());
                    assertEquals(1,message.size());

                    final var body = new JSONObject(message.getFirst().body());

                    final var innerMessage = new JSONObject(body.getString("Message"));
                    assertNotNull(innerMessage.getString("id"));
                    assertEquals(sale.cpf(), innerMessage.getString("cpf"));
                    assertEquals(sale.vin(), innerMessage.getString("vin"));
                    assertNotNull(innerMessage.getString("registrationDate"));

                });
    }

    @DisplayName("Given a request to create a sale with a car that is already sold, return 500")
    @Test
    void givenInvalidRequestToCreateASale(){
        final var sale = createSalesDtoRequest("12345678900","1HGCM82633A123456");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(sale)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("data.details", equalTo("This car was already sold"));
    }

    @DisplayName("Get a sale by ID with a valid request")
    @Test
     void givenValidRequestToGetASaleThenGetIt(){
        final var cpf = "10987654321";
        final var vin = "1HGCM82633A678901";

        RestAssured.given()
                .pathParam("id", "3b4d42ed-9554-4e9b-a7f8-cbf49688e8d4")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_ID_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", equalTo("3b4d42ed-9554-4e9b-a7f8-cbf49688e8d4"))
                .body("data.cpf", equalTo(cpf))
                .body("data.vin", equalTo(vin));
    }

    @DisplayName("Given a ID that is not registered, return 404")
    @Test
     void givenIdThatIsNotRegisteredReturnNotFound(){
        RestAssured.given()
                .pathParam("id", UUID.randomUUID().toString())
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_ID_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all sales")
    @Test
     void givenValidRequestGetAllSales(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data",notNullValue())
                .body("data.content",notNullValue())
                .body("data.page.size",equalTo(10))
                .body("data.page.totalElements",equalTo(1));
    }

    @DisplayName("Given a valid request to cancel a sale, do it")
    @Test
     void givenValidRequestToDeleteCarDoIt(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id","b66f0363-7981-4db3-b756-d1226057382d")
                .when()
                .delete(URL_WITH_ID_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Given a request with a invalid id to cancel a sale, return 404")
    @Test
     void givenRequestWithInvalidIdToCancelSaleReturnNotFound(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete(URL_WITH_ID_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private static SalesDtoRequest createSalesDtoRequest(String cpf ,String vin){
        return SalesDtoRequest.builder()
                .cpf(cpf)
                .vin(vin)
                .build();
    }
}