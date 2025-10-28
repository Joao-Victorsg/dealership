package br.com.dealership.client.api.adapter.out.gateway.service;

import br.com.dealership.client.api.adapter.out.gateway.mapper.KeycloakMapper;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.usecase.port.KeycloakServicePort;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
public class KeycloakService implements KeycloakServicePort {

    private final KeycloakMapper keycloakMapper;
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public String createUser(ClientModel clientModel) {
        final var user = keycloakMapper.toKeyCloakUserDto(clientModel);

        final var response = keycloak.realm(realm)
                .users()
                .create(user);

        final var userId = extractUserIdFromLocation(response.getLocation());
        response.close();

        return userId;
    }

    @Override
    public void sendVerificationEmail(String userId) {
        keycloak.realm(realm)
                .users()
                .get(userId)
                .executeActionsEmail(List.of("VERIFY_EMAIL"));
    }

    @Override
    public void deleteUser(String userId) {
        keycloak.realm(realm)
                .users()
                .get(userId)
                .remove();
    }

    private String extractUserIdFromLocation(URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}