package br.com.dealership.client.api.core.usecase.port;

import br.com.dealership.client.api.core.domain.ClientModel;

public interface KeycloakServicePort {
    String createUser(ClientModel clientModel);

    void sendVerificationEmail(String userId);

    void deleteUser(String userId);
}