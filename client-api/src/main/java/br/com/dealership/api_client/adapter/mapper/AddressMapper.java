package br.com.dealership.api_client.adapter.mapper;

import br.com.dealership.api_client.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.response.AddressDtoResponse;
import br.com.dealership.api_client.adapter.out.database.entity.AddressEntity;
import br.com.dealership.api_client.adapter.out.gateway.dto.AddressDtoGateway;
import br.com.dealership.api_client.core.domain.AddressModel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddressMapper {

    public AddressModel toModel(AddressDtoRequest addressDtoRequest){
        return AddressModel.builder()
                .postCode(addressDtoRequest.postCode())
                .streetNumber(addressDtoRequest.streetNumber())
                .build();
    }

    public AddressModel toModel(AddressEntity addressEntity){
        return AddressModel.builder()
                .id(UUID.fromString(addressEntity.getId()))
                .postCode(addressEntity.getPostCode())
                .streetName(addressEntity.getStreetName())
                .streetNumber(addressEntity.getStreetNumber())
                .city(addressEntity.getCity())
                .stateAbbreviation(addressEntity.getStateAbbreviation())
                .isAddressSearched(addressEntity.getIsAddressSearched())
                .build();
    }

    public AddressEntity toEntity(AddressModel addressModel){
        return AddressEntity.builder()
                .postCode(addressModel.postCode())
                .city(addressModel.city())
                .stateAbbreviation(addressModel.stateAbbreviation())
                .streetName(addressModel.streetName())
                .streetNumber(addressModel.streetNumber())
                .isAddressSearched(addressModel.isAddressSearched())
                .build();
    }

    public AddressModel toModel(AddressModel addressModel, AddressDtoGateway addressDtoGateway){
        return addressModel.toBuilder()
                .city(addressDtoGateway.city())
                .streetName(addressDtoGateway.streetName())
                .stateAbbreviation(addressDtoGateway.stateAbbreviation())
                .isAddressSearched(addressDtoGateway.isAddressSearched())
                .build();
    }

    public AddressDtoResponse toDtoResponse(AddressModel addressModel){
        return AddressDtoResponse.builder()
                .postCode(addressModel.postCode())
                .city(addressModel.city())
                .streetName(addressModel.streetName())
                .stateAbbreviation(addressModel.stateAbbreviation())
                .streetNumber(addressModel.streetNumber())
                .isAddressSearched(addressModel.isAddressSearched())
                .build();
    }
}
