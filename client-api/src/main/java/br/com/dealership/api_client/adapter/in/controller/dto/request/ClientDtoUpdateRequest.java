package br.com.dealership.api_client.adapter.in.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = ClientDtoUpdateRequest.ClientDtoUpdateRequestBuilder.class)
public record ClientDtoUpdateRequest (
    @NotBlank @JsonProperty String postCode,
    @NotBlank @JsonProperty String streetNumber
){}