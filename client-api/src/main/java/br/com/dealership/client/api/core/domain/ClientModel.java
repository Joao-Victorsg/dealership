package br.com.dealership.client.api.core.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record ClientModel(
    String cpf,
    String name,
    AddressModel clientAddress,
    String email,
    LocalDateTime registrationDate
) {

    public static ClientModel of(ClientModel clientModel, AddressModel addressModel){
        return clientModel.toBuilder()
                .clientAddress(addressModel)
                .build();
    }

}