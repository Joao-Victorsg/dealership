package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateClientUseCase {

    private final ClientServicePort clientServicePort;
    private final AddressServicePort addressServicePort;

    public ClientModel execute(String cpf, AddressModel addressModel) throws ClientNotFoundException {
        final var searchedAddress = addressServicePort.search(addressModel);

        return clientServicePort.update(cpf,searchedAddress);
    }
}
