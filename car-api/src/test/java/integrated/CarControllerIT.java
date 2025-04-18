package integrated;

import br.com.dealership.car.api.adapter.in.dto.request.CarDtoRequest;
import br.com.dealership.car.api.adapter.in.dto.request.CarDtoUpdateRequest;
import integrated.utils.SqsClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.Duration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@RequiredArgsConstructor
 class CarControllerIT extends BaseIT{

    private static final String QUEUE_NAME = "client-car-creation-event-queue";
    private static final String URL_WITH_VIN_PATH_PARAMETER = "/v1/dealership/cars/{vin}";
    private static final String URL = "/v1/dealership/cars";

    @DisplayName("Given a valid request to create a car then create it")
    @Test
     void givenValidRequestToCreateACarThenCreateIt(){
       final var car = createCarDtoRequest("12345678911");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.id", notNullValue())
                .body("data.model", equalTo(car.model()))
                .body("data.modelYear", equalTo(car.modelYear()))
                .body("data.manufacturer",equalTo(car.manufacturer()))
                .body("data.vin", equalTo(car.vin()))
                .body("data.value", equalTo(car.value().floatValue()));


        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    final var message = SqsClient.getMessageFromQueue(QUEUE_NAME);
                    assertNotNull(message);
                    assertTrue(message.isPresent());

                    final var eventTypeHeader = message.get().getHeaders().get("eventType");
                    final var body = message.get().getPayload();

                    assertEquals("CarCreated", eventTypeHeader);
                    assertEquals(car.vin(), body);
                });
    }

    @DisplayName("Get a car by VIN with a valid request")
    @Test
     void givenValidRequestToGetACarThenGetIt(){
        final var car = createCarDtoRequest("12345678911");

        RestAssured.given()
                .pathParam("vin", "12345678911")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.id", notNullValue())
                .body("data.model", equalTo(car.model()))
                .body("data.modelYear", equalTo(car.modelYear()))
                .body("data.manufacturer",equalTo(car.manufacturer()))
                .body("data.vin", equalTo(car.vin()))
                .body("data.value", equalTo(car.value().floatValue()));
    }

    @DisplayName("Given a VIN that is not registered, return 404")
    @Test
     void givenVINThatIsNotRegisteredReturnNotFound(){
        RestAssured.given()
                .pathParam("vin", "1")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all cars")
    @Test
     void givenValidRequestGetAllCars(){
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

    @DisplayName("Given a valid request to update a car, do it")
    @Test
     void givenValidRequestToUpdateCarDoIt(){
        final var carDto = createCarDtoRequest("12345679872");
        createCar(carDto);
        final var carDtoUpdate = createCarDtoUpdateRequest("red",BigDecimal.valueOf(11111.11));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(carDtoUpdate)
                .pathParam("vin","12345679872")
                .when()
                .put(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.color",equalTo(carDtoUpdate.color()))
                .body("data.value",equalTo(carDtoUpdate.value().floatValue()))
                .body("data.model", equalTo(carDto.model()))
                .body("data.modelYear", equalTo(carDto.modelYear()))
                .body("data.manufacturer",equalTo(carDto.manufacturer()))
                .body("data.vin", equalTo(carDto.vin()));
    }

    @DisplayName("Given a request with a invalid VIN to update a car, return 404")
    @Test
     void givenRequestWithInvalidVINToUpdateCarReturnNotFound(){
        final var carDto = createCarDtoUpdateRequest("red",BigDecimal.valueOf(111111.00));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(carDto)
                .pathParam("vin","11987654321")
                .when()
                .put(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request to delete a car, do it")
    @Test
     void givenValidRequestToDeleteCarDoIt(){
        final var carDto = createCarDtoRequest("1HGCM82633A123456");
        createCar(carDto);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("vin","1HGCM82633A123456")
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("Given a request with a invalid vin to delete a car, return 404")
    @Test
     void givenRequestWithInvalidVINToDeleteCarReturnNotFound(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("vin","0")
                .when()
                .delete(URL_WITH_VIN_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private static void createCar(CarDtoRequest carDtoRequest){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(carDtoRequest)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    private static CarDtoUpdateRequest createCarDtoUpdateRequest(String color, BigDecimal value){
        return CarDtoUpdateRequest.builder()
                .color(color)
                .value(value)
                .build();
    }

    private static CarDtoRequest createCarDtoRequest(String vin){
        return CarDtoRequest.builder()
                .color("Black")
                .manufacturer("Audi")
                .model("A3")
                .modelYear("2021")
                .vin(vin)
                .value(BigDecimal.valueOf(100000.0))
                .build();
    }
}