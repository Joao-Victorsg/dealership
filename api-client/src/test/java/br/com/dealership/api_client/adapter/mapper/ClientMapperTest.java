package br.com.dealership.api_client.adapter.mapper;

import br.com.dealership.api_client.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.response.AddressDtoResponse;
import br.com.dealership.api_client.adapter.in.controller.dto.response.ClientDtoResponse;
import br.com.dealership.api_client.adapter.out.database.entity.AddressEntity;
import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientMapperTest {
    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private ClientMapper clientMapper;

    @Test
    void shouldMapClientDtoRequestToModel() {
        final var addressDtoRequest = AddressDtoRequest.builder()
                .postCode("12345")
                .streetNumber("10")
                .build();

        final var clientDtoRequest = ClientDtoRequest.builder()
                .cpf("12345678900")
                .name("John Doe")
                .address(addressDtoRequest)
                .build();

        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetNumber("10")
                .build();

        when(addressMapper.toModel(addressDtoRequest)).thenReturn(addressModel);

        final var clientModel = clientMapper.toModel(clientDtoRequest);

        assertEquals(clientDtoRequest.cpf(), clientModel.cpf());
        assertEquals(clientDtoRequest.name(), clientModel.name());
        assertEquals(addressModel, clientModel.clientAddress());
    }

    @Test
    void shouldMapClientEntityToModel() {
        final var addressEntity = AddressEntity.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        final var clientEntity = ClientEntity.builder()
                .cpf("12345678900")
                .name("John Doe")
                .address(addressEntity)
                .registrationDate(LocalDateTime.of(2023, 11, 26, 12, 0))
                .build();

        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        when(addressMapper.toModel(addressEntity)).thenReturn(addressModel);

        final var clientModel = clientMapper.toModel(clientEntity);

        assertEquals(clientEntity.getCpf(), clientModel.cpf());
        assertEquals(clientEntity.getName(), clientModel.name());
        assertEquals(clientEntity.getRegistrationDate(), clientModel.registrationDate());
        assertEquals(addressModel, clientModel.clientAddress());
    }

    @Test
    void shouldMapClientModelToEntity() {
        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        final var clientModel = ClientModel.builder()
                .cpf("12345678900")
                .name("John Doe")
                .clientAddress(addressModel)
                .build();

        final var addressEntity = AddressEntity.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        when(addressMapper.toEntity(addressModel)).thenReturn(addressEntity);

        final var clientEntity = clientMapper.toEntity(clientModel);

        assertEquals(clientModel.cpf(), clientEntity.getCpf());
        assertEquals(clientModel.name(), clientEntity.getName());
        assertEquals(addressEntity, clientEntity.getAddress());
    }

    @Test
    void shouldMapClientModelToDtoResponse() {
        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        final var clientModel = ClientModel.builder()
                .cpf("12345678900")
                .name("John Doe")
                .clientAddress(addressModel)
                .registrationDate(LocalDateTime.of(2023, 11, 26, 12, 0))
                .build();

        final var addressDtoResponse = AddressDtoResponse.builder()
                .postCode("12345")
                .streetName("Main St")
                .build();

        when(addressMapper.toDtoResponse(addressModel)).thenReturn(addressDtoResponse);

        ClientDtoResponse clientDtoResponse = clientMapper.toDtoResponse(clientModel);

        assertEquals(clientModel.cpf(), clientDtoResponse.cpf());
        assertEquals(clientModel.name(), clientDtoResponse.name());
        assertEquals(clientModel.registrationDate(), clientDtoResponse.registrationDate());
        assertEquals(addressDtoResponse, clientDtoResponse.address());
    }
}