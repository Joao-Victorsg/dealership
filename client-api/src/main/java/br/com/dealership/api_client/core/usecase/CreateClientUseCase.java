package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.usecase.port.AddressServicePort;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
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