package br.com.dealership.sales_api.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SalesDtoRequest(
        @NotBlank
        String cpf,
        @NotBlank
        String vin
) {
}