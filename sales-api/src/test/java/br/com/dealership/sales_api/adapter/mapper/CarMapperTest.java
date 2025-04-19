package br.com.dealership.sales_api.adapter.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarMapperTest {

    private final CarMapper carMapper = new CarMapper();

    @Test
    void shouldMapToEntity(){
        final var vin = "ABC1234";
        final var carEntity = carMapper.toEntity(vin);

        assertNotNull(carEntity);
        assertEquals(vin, carEntity.getVin());
    }

}