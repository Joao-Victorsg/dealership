package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetClientUseCase {

    private final ClientServicePort clientServicePort;

    public ClientModel execute(String cpf) throws ClientNotFoundException {
        return clientServicePort.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("There isn't a client with this CPF"));
    }

    public Page<ClientModel> execute(Pageable pageable, String city, String state) {
        return clientServicePort.getAll(city, state,pageable);
    }
}