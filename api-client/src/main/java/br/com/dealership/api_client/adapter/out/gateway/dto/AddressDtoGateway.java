package br.com.dealership.api_client.adapter.out.gateway.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressDtoGateway (
        @NotBlank
        @JsonAlias("cep")
        String postCode,
        @JsonAlias("localidade")
        String city,
        @JsonAlias("uf")
        String stateAbbreviation,
        @JsonAlias("logradouro")
        String streetName,
        Boolean isAddressSearched
){

    @Builder
    public AddressDtoGateway(String postCode,
                             String city,
                             String stateAbbreviation,
                             String streetName,
                             Boolean isAddressSearched) {
        this.postCode = postCode;
        this.city = city;
        this.stateAbbreviation = stateAbbreviation;
        this.streetName = streetName;
        this.isAddressSearched = verifyIfAddressWasSearched(isAddressSearched);
    }

    private boolean verifyIfAddressWasSearched(Boolean isAddressSearched) {
        return ObjectUtils.anyNull(isAddressSearched);
    }
}
