package br.com.dealership.api_client.adapter.in.controller.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public final class ResponseError {

    @NotNull(message="Timestamp cannot be null")
    private final LocalDateTime timestamp;

    @NotNull(message="Details cannot be null")
    private final String details;
}