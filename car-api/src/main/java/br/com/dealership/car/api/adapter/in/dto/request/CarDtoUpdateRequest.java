package br.com.dealership.car.api.adapter.in.dto.request;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CarDtoUpdateRequest(String color, BigDecimal value, String modelYear) {
}
