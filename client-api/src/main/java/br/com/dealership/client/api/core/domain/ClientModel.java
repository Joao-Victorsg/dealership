package br.com.dealership.client.api.core.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record ClientModel(
    String cpf,
    String keycloakId,
    String name,
    AddressModel clientAddress,
    String email,
    String password,
    String phoneNumber,
    LocalDateTime registrationDate
) {

    public static ClientModel of(ClientModel clientModel, AddressModel addressModel, String keycloakUserId) {
        return clientModel.toBuilder()
                .keycloakId(keycloakUserId)
                .clientAddress(addressModel)
                .build();
    }

}