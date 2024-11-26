package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetClientUseCase {

    private final ClientServicePort clientServicePort;

    public ClientModel execute(String cpf) throws ClientNotFoundException {
        return clientServicePort.findByCpf(cpf);
    }

    public Page<ClientModel> execute(Pageable pageable, String city, String state) {
        return clientServicePort.getAll(city, state,pageable);
    }
}