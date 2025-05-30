package integrated;

import br.com.dealership.client.api.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.client.api.adapter.in.controller.dto.request.ClientDtoRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static integrated.wiremock.MockServer.mockGetAddressByPostCode;
import static integrated.wiremock.MockServer.mockGetAddressByPostCodeWithSlowResponse;
import static integrated.wiremock.MockServer.mockGetAddressByPostCodeWithUnavailable;

class ResilienceClientControllerIT extends BaseIT{

    private static final String URL = "/v1/dealership/clients";

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @DisplayName("Deve retornar para CLOSED após uma chamada bem-sucedida")
    @Test
    void shouldTransitionToClosedAfterSuccessfulCall(){
        final var circuitBreaker = circuitBreakerRegistry.circuitBreaker("searchAddressGatewaybyPostCode");

        for (int i = 0; i < 10; i++) {
            mockGetAddressByPostCodeWithUnavailable("39999-999");
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("123466789" + i, "emaiil" + i + "@email.com", "39999-999"));
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState()));

        Awaitility.await()
                .pollDelay(5, TimeUnit.SECONDS)
                .atLeast(Duration.ofSeconds(5))
                .until(() -> true);

        mockGetAddressByPostCode("39999-988");

        createClientThroughPostRequest(createClientDtoRequestWithPostCode("1244567991" + 0, "emaiil11" + 0 + "@email.com", "39999-988"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState()));

        for (int i = 1; i < 10; i++) {
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("1224567991" + i, "emaila11" + i + "@email.com", "39999-988"));
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState()));
    }

    @DisplayName("Deve transitar para HALF_OPEN após o tempo de espera")
    @Test
    void shouldTransitionToHalfOpenAfterWaitDuration(){
        final var circuitBreaker = circuitBreakerRegistry.circuitBreaker("searchAddressGatewaybyPostCode");

        for (int i = 0; i < 10; i++) {
            mockGetAddressByPostCodeWithUnavailable("39999-999");
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("123456489" + i, "emai" + i + "@email.com", "39999-999"));
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState()));

        Awaitility.await()
                .pollDelay(2, TimeUnit.SECONDS)
                .atLeast(Duration.ofSeconds(2))
                .until(() -> true);

        createClientThroughPostRequest(createClientDtoRequestWithPostCode("1234562991" + 0, "emai21" + 0 + "@email.com", "39999-988"));

        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState()));
    }

    @DisplayName("Should be OPEN after multiples failures")
    @Test
    void shouldRemainOpenAfterNewFailures() {
        final var circuitBreaker = circuitBreakerRegistry.circuitBreaker("searchAddressGatewaybyPostCode");

        for (int i = 0; i < 10; i++) {
            mockGetAddressByPostCodeWithUnavailable("39999-999");
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("123456689" + i, "emaill" + i + "@email.com", "39999-999"));
        }

        Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .untilAsserted(() -> Assertions.assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState()));

        for (int i = 10; i < 15; i++) {
            mockGetAddressByPostCodeWithUnavailable("39999-999");
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("123451689" + i, "emaill" + i + "@email.com", "39999-999"));
        }

        Assertions.assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    @DisplayName("Should OPEN the circuit breaker after multiple slowcalls")
    @Test
    void shouldOpenCircuitBreakerAfterMultipleSlowCalls() {
        final var circuitBreaker = circuitBreakerRegistry.circuitBreaker("searchAddressGatewaybyPostCode");

        for (int i = 0; i < 5; i++) {
            mockGetAddressByPostCodeWithSlowResponse("40000-999");
            createClientThroughPostRequest(createClientDtoRequestWithPostCode("123456189" + i, "emailo" + i + "@email.com", "40000-999"));

        }
        Assertions.assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    private ClientDtoRequest createClientDtoRequestWithPostCode(String cpf, String email, String postCode) {
        return ClientDtoRequest.builder()
                .name("teste-integrado")
                .cpf(cpf)
                .address(AddressDtoRequest.builder()
                        .postCode(postCode)
                        .streetNumber("321")
                        .build())
                .email(email)
                .build();
    }

    private void createClientThroughPostRequest(ClientDtoRequest client) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
    }
}
