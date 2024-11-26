package br.com.dealership.api_client.adapter.mapper;

import br.com.dealership.api_client.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.api_client.adapter.out.database.entity.AddressEntity;
import br.com.dealership.api_client.adapter.out.gateway.dto.AddressDtoGateway;
import br.com.dealership.api_client.core.domain.AddressModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {

    private final AddressMapper addressMapper = new AddressMapper();

    @Test
    void shouldMapAddressDtoRequestToModel() {
        final var addressDtoRequest = AddressDtoRequest.builder()
                .postCode("12345")
                .streetNumber("10")
                .build();

        final var addressModel = addressMapper.toModel(addressDtoRequest);

        assertEquals(addressDtoRequest.postCode(), addressModel.postCode());
        assertEquals(addressDtoRequest.streetNumber(), addressModel.streetNumber());
    }

    @Test
    void shouldMapAddressEntityToModel() {
        final var addressEntity = AddressEntity.builder()
                .id(UUID.randomUUID().toString())
                .postCode("12345")
                .streetName("Main St")
                .streetNumber("10")
                .city("Springfield")
                .stateAbbreviation("IL")
                .isAddressSearched(true)
                .build();

        final var addressModel = addressMapper.toModel(addressEntity);

        assertEquals(UUID.fromString(addressEntity.getId()), addressModel.id());
        assertEquals(addressEntity.getPostCode(), addressModel.postCode());
        assertEquals(addressEntity.getStreetName(), addressModel.streetName());
        assertEquals(addressEntity.getStreetNumber(), addressModel.streetNumber());
        assertEquals(addressEntity.getCity(), addressModel.city());
        assertEquals(addressEntity.getStateAbbreviation(), addressModel.stateAbbreviation());
        assertEquals(addressEntity.getIsAddressSearched(), addressModel.isAddressSearched());
    }

    @Test
    void shouldMapAddressModelToEntity() {
        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetName("Main St")
                .streetNumber("10")
                .city("Springfield")
                .stateAbbreviation("IL")
                .isAddressSearched(true)
                .build();

        final var addressEntity = addressMapper.toEntity(addressModel);

        assertEquals(addressModel.postCode(), addressEntity.getPostCode());
        assertEquals(addressModel.streetName(), addressEntity.getStreetName());
        assertEquals(addressModel.streetNumber(), addressEntity.getStreetNumber());
        assertEquals(addressModel.city(), addressEntity.getCity());
        assertEquals(addressModel.stateAbbreviation(), addressEntity.getStateAbbreviation());
        assertEquals(addressModel.isAddressSearched(), addressEntity.getIsAddressSearched());
    }

    @Test
    void shouldMapAddressModelAndGatewayToModel() {
        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .build();

        final var addressDtoGateway = AddressDtoGateway.builder()
                .city("Springfield")
                .streetName("Main St")
                .stateAbbreviation("IL")
                .isAddressSearched(true)
                .build();

        final var updatedModel = addressMapper.toModel(addressModel, addressDtoGateway);

        assertEquals(addressModel.postCode(), updatedModel.postCode());
        assertEquals(addressDtoGateway.city(), updatedModel.city());
        assertEquals(addressDtoGateway.streetName(), updatedModel.streetName());
        assertEquals(addressDtoGateway.stateAbbreviation(), updatedModel.stateAbbreviation());
        assertEquals(addressDtoGateway.isAddressSearched(), updatedModel.isAddressSearched());
    }

    @Test
    void shouldMapAddressModelToDtoResponse() {
        final var addressModel = AddressModel.builder()
                .postCode("12345")
                .streetName("Main St")
                .streetNumber("10")
                .city("Springfield")
                .stateAbbreviation("IL")
                .isAddressSearched(true)
                .build();

        final var addressDtoResponse = addressMapper.toDtoResponse(addressModel);

        assertEquals(addressModel.postCode(), addressDtoResponse.postCode());
        assertEquals(addressModel.streetName(), addressDtoResponse.streetName());
        assertEquals(addressModel.streetNumber(), addressDtoResponse.streetNumber());
        assertEquals(addressModel.city(), addressDtoResponse.city());
        assertEquals(addressModel.stateAbbreviation(), addressDtoResponse.stateAbbreviation());
        assertEquals(addressModel.isAddressSearched(), addressDtoResponse.isAddressSearched());
    }
}