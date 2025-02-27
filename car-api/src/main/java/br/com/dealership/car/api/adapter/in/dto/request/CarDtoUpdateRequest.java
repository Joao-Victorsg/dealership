package br.com.dealership.car.api.adapter.in.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CarDtoUpdateRequest(String color,
                                  BigDecimal value) {}