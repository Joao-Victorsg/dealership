package br.com.dealership.client.api.adapter.out.gateway.mapper;

import br.com.dealership.client.api.adapter.out.gateway.enums.KeycloakRealmRoles;
import br.com.dealership.client.api.core.domain.ClientModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakMapper {

    public UserRepresentation toKeyCloakUserDto(ClientModel clientModel){

        final var user = new UserRepresentation();
        user.setUsername(clientModel.email());
        user.setFirstName(clientModel.name());
        user.setLastName(clientModel.name());
        user.setEmail(clientModel.email());
        user.setEmailVerified(false);
        user.setEnabled(true);
        user.setRealmRoles(List.of(KeycloakRealmRoles.CLIENT.name()));
        user.setRequiredActions(List.of("VERIFY_EMAIL"));
        user.setCredentials(List.of(toCredentialRepresentation(clientModel.password())));

        return user;
    }

    private CredentialRepresentation toCredentialRepresentation(String password){
        final var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }
}
