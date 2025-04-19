package br.com.dealership.sales_api.adapter.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientMapperTest {

    private final ClientMapper clientMapper = new ClientMapper();

    @Test
    void shouldMapToEntity(){
        final var cpf = "12345678900";
        final var clientEntity = clientMapper.toEntity(cpf);

        assertNotNull(clientEntity);
        assertEquals(cpf, clientEntity.getCpf());
    }
}