package br.com.dealership.client.api.adapter.in.controller.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientDtoResponse(
    @NotBlank String name,
    @NotBlank @Size(min = 11,max = 11) String cpf,
    @NotNull AddressDtoResponse address,
    @NotNull LocalDateTime registrationDate,
    @NotNull String email,
    @NotBlank String phoneNumber
){}