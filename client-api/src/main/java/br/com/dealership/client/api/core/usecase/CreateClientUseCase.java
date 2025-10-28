package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.client.api.core.exceptions.EmailAlreadyInUseException;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import br.com.dealership.client.api.core.usecase.port.KeycloakServicePort;
import br.com.dealership.client.api.core.usecase.port.SendCreationEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateClientUseCase {

    private final ClientServicePort clientServicePort;
    private final AddressServicePort addressServicePort;
    private final KeycloakServicePort keycloakServicePort;
    private final SendCreationEventPort sendCreationEventPort;

    public ClientModel execute(ClientModel clientModel) throws ClientAlreadyExistsException, EmailAlreadyInUseException {
        if(clientServicePort.findByCpf(clientModel.cpf()).isPresent())
            throw new ClientAlreadyExistsException("A client with this CPF already exists");

        if(clientServicePort.existsByEmail(clientModel.email()))
            throw new EmailAlreadyInUseException("The provided email is already in use");

        final var searchedAddressModel = addressServicePort.search(clientModel.clientAddress());

        final var keycloadkId = keycloakServicePort.createUser(clientModel);

        try{
            final var clientWithSearchedAddress = ClientModel.of(clientModel, searchedAddressModel, keycloadkId);

            final var createdClientModel = clientServicePort.create(clientWithSearchedAddress);

            keycloakServicePort.sendVerificationEmail(keycloadkId);

            sendCreationEventPort.sendCreationEvent(createdClientModel.cpf());

            return createdClientModel;
        }catch (Exception e){
            //TODO: Ajustar as exceções
            keycloakServicePort.deleteUser(keycloadkId);
            throw e;
        }
    }
}