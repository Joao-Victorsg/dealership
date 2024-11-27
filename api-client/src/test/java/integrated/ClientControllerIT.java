package integrated;

import br.com.dealership.api_client.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static integrated.wiremock.MockServer.mockGetAddressByPostCode;
import static integrated.wiremock.MockServer.mockGetAddressByPostCodeWithServerError;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RequiredArgsConstructor
class ClientControllerIT extends BaseIT {
    private static final String URL_WITH_CPF_PATH_PARAMETER = "/v1/dealership/clients/{cpf}";
    private static final String URL = "/v1/dealership/clients";

    @DisplayName("Given a valid request to create a client then create it")
    @Test
    void givenValidRequestToCreateAClienteThenCreateIt() {
        final var client = createClientDtoRequest("84531547812");
        mockGetAddressByPostCode("39999-999");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.name", equalTo(client.name()))
                .body("data.cpf", equalTo(client.cpf()))
                .body("data.address.postCode", equalTo("39999-999"))
                .body("data.address.streetName", equalTo("Test Street"))
                .body("data.address.stateAbbreviation", equalTo("TT"))
                .body("data.address.city", equalTo("Test"));
    }

    @DisplayName("Should create the client even if the via cep api returns an exception")
    @Test
    void shouldCreateClientEvenIfViaCepApiReturnsAnException() {
        final var client = createClientDtoRequest("12345678912");
        mockGetAddressByPostCodeWithServerError("39999-999");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.name", equalTo(client.name()))
                .body("data.cpf", equalTo(client.cpf()))
                .body("data.address.postCode", equalTo("39999-999"))
                .body("data.address.isAddressSearched", equalTo(false));
    }

    @DisplayName("Get a client by CPF with a valid request")
    @Test
    void givenValidRequestToGetAClientThenGetIt() {
        final var client = createClientDtoRequest("11987654321");
        mockGetAddressByPostCode("39999-999");

        createClientThroughPostRequest(client);

        RestAssured.given()
                .pathParam("cpf", "11987654321")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo(client.name()))
                .body("data.cpf", equalTo(client.cpf()))
                .body("data.address.postCode", equalTo(client.address().postCode()))
                .body("data.address.streetName", equalTo("Test Street"))
                .body("data.address.stateAbbreviation", equalTo("TT"))
                .body("data.address.city", equalTo("Test"));
    }


    @DisplayName("Given a CPF that is not registered, return 404")
    @Test
    void givenCPFThatIsNotRegisteredReturnNotFound() {
        RestAssured.given()
                .pathParam("cpf", "1")
                .contentType(ContentType.JSON)
                .when()
                .get(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request get all clients")
    @Test
    void givenValidRequestGetAllClients() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data", notNullValue())
                .body("data.content", notNullValue())
                .body("data.size", equalTo(10));
    }

    @DisplayName("Given a valid request to update a client, do it")
    @Test
    void givenValidRequestToUpdateClientDoIt(){
        final var client = createClientDtoRequest("22222222222");
        final var clientDto = createClientUpdateDtoRequest("38888-888", "123");
        mockGetAddressByPostCode("39999-999");
        mockGetAddressByPostCode("38888-888");

        createClientThroughPostRequest(client);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(clientDto)
                .pathParam("cpf", "22222222222")
                .when()
                .put(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data.name", equalTo("teste-integrado"))
                .body("data.address.postCode",equalTo("38888-888"))
                .body("data.address.city", equalTo("Test"))
                .body("data.address.stateAbbreviation", equalTo("TT"))
                .body("data.address.streetName", equalTo("Test Street"));
    }

    @DisplayName("Given a request with a invalid cpf to update a client, return 404")
    @Test
    void givenRequestWithInvalidCpfToUpdateClientReturnNotFound(){
        final var clientDto = createClientUpdateDtoRequest("38888888", "123");
        mockGetAddressByPostCode("39999-999");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(clientDto)
                .pathParam("cpf", "11987654321")
                .when()
                .put(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Given a valid request to delete a client, do it")
    @Test
    void givenValidRequestToDeleteClientDoIt(){
        final var client = createClientDtoRequest("48932486544");

        mockGetAddressByPostCode("39999-999");
        createClientThroughPostRequest(client);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("cpf", "48932486544")
                .when()
                .delete(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.OK.value());

    }

    @DisplayName("Given a request with a invalid cpf to delete a client, return 404")
    @Test
    void givenRequestWithInvalidCpfToDeleteClientReturnNotFound() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("cpf", "0")
                .when()
                .delete(URL_WITH_CPF_PATH_PARAMETER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
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

    private ClientDtoUpdateRequest createClientUpdateDtoRequest(String postCode, String streetNumber) {
        return ClientDtoUpdateRequest.builder()
                .postCode(postCode)
                .streetNumber(streetNumber)
                .build();
    }

    private ClientDtoRequest createClientDtoRequest(String cpf) {
        return ClientDtoRequest.builder()
                .name("teste-integrado")
                .cpf(cpf)
                .address(AddressDtoRequest.builder()
                        .postCode("39999-999")
                        .streetNumber("321")
                        .build())
                .build();
    }
}