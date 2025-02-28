package br.com.dealership.car.api.core.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SearchFilter(
        BigDecimal initialValue,
        BigDecimal finalValue,
        String modelYear,
        String model,
        String manufacturer
){
}
