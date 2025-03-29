package br.com.dealership.api_client.adapter.in.controller.dto.response;

import lombok.Builder;

@Builder
public record AddressDtoResponse(
        String postCode,
        String city,
        String stateAbbreviation,
        String streetName,
        String streetNumber,
        Boolean isAddressSearched
) {}
