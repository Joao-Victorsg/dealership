package br.com.dealership.api_client.core.domain;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record AddressModel(
        UUID id,
        String city,
        String postCode,
        String stateAbbreviation,
        String streetName,
        String streetNumber,
        Boolean isAddressSearched
) {
}