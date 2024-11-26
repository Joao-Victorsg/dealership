package br.com.dealership.api_client.core.domain;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record ClientModel(
    String cpf,
    String name,
    AddressModel clientAddress,
    LocalDateTime registrationDate
) {

    public static ClientModel of(ClientModel clientModel, AddressModel addressModel){
        return clientModel.toBuilder()
                .clientAddress(addressModel)
                .build();
    }

}