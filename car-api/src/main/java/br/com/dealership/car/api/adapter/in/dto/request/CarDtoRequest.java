package br.com.dealership.car.api.adapter.in.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@JsonDeserialize(builder = CarDtoRequest.CarDtoRequestBuilder.class)
public record CarDtoRequest (
        @JsonProperty @NotBlank String model,
        @JsonProperty @NotBlank String modelYear,
        @JsonProperty @NotBlank String manufacturer,
        @JsonProperty @NotBlank String color,
        @JsonProperty @NotBlank String vin,
        @JsonProperty BigDecimal value){
}