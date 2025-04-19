package br.com.dealership.sales_api.adapter.in.controller.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseError (
    @NotNull(message="Timestamp cannot be null")
    LocalDateTime timestamp,

    @NotNull(message="Details cannot be null")
    String details
    ){
}
