package br.com.dealership.car.api.adapter.mapper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchFilterMapperTest {

    private final SearchFilterMapper searchFilterMapper = new SearchFilterMapper();

    @Test
    void shouldMapWithSuccess(){
        final var initialValue = BigDecimal.ZERO;
        final var finalValue = BigDecimal.ZERO;
        final var model = "Cruze";
        final var modelYear = "1998";
        final var manufacturer = "GM";

        final var resultado = searchFilterMapper.toSearchFilter(initialValue,finalValue,model,modelYear,manufacturer);

        assertEquals(initialValue,resultado.initialValue());
        assertEquals(finalValue,resultado.finalValue());
        assertEquals(model,resultado.model());
        assertEquals(modelYear,resultado.modelYear());
        assertEquals(manufacturer,resultado.manufacturer());
    }
}