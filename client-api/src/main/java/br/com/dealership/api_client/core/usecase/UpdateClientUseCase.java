package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.AddressServicePort;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
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
