package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateClientUseCase {

    private final ClientServicePort clientServicePort;
    private final AddressServicePort addressServicePort;

    public ClientModel execute(ClientModel clientModel) throws ClientAlreadyExistsException {
        final var searchedAddressModel = addressServicePort.search(clientModel.clientAddress());

        final var clientWithSearchedAddress = ClientModel.of(clientModel,searchedAddressModel);

        return clientServicePort.create(clientWithSearchedAddress);
    }
}