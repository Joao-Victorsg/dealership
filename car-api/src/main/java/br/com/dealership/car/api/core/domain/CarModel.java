package br.com.dealership.car.api.core.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record CarModel(
        UUID id,
        String model,
        String modelYear,
        String manufacturer,
        String color,
        String vin,
        BigDecimal value,
        LocalDateTime registrationDate
) {
}