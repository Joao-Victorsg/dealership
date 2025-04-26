package br.com.dealership.sales_api.core.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SalesModel (
        UUID id,
        String cpf,
        String vin,
        LocalDateTime registrationDate
){
}