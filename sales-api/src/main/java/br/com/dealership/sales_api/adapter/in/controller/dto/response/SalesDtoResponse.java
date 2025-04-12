package br.com.dealership.sales_api.adapter.in.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SalesDtoResponse (
        UUID id,
        String cpf,
        String vin,
        LocalDateTime registrationDate
){
}