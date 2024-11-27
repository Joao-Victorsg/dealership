package integrated.wiremock;

import br.com.dealership.api_client.adapter.out.gateway.dto.AddressDtoGateway;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static integrated.container.WiremockContainerDefinition.getWiremockHost;
import static integrated.container.WiremockContainerDefinition.getWiremockPort;

public final class MockServer {

    private static final Gson JSON_MAPPER = new Gson();

    static {
        WireMock.configureFor(getWiremockHost(),getWiremockPort());
    }

    public static void mockGetAddressByPostCode(String postCode){
        final var VIA_CEP_PATH = "/ws/" + postCode + "/json";
        final var addressMock = addressMock(postCode);

        WireMock.stubFor(get(urlMatching(VIA_CEP_PATH))
                .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(JSON_MAPPER.toJson(addressMock)))
        );
    }

    public static void mockGetAddressByPostCodeWithServerError(String postCode){
        final String VIA_CEP_PATH="/ws/" + postCode + "/json";
            WireMock.stubFor(get(urlMatching(VIA_CEP_PATH))
                    .willReturn(serverError()));
    }

    private static AddressDtoGateway addressMock(String postCode){
        return AddressDtoGateway.builder()
                .postCode(postCode)
                .city("Test")
                .stateAbbreviation("TT")
                .streetName("Test Street")
                .build();
    }

}