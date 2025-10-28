package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteClientUseCase {

    private final ClientServicePort clientServicePort;

    public void execute(String cpf) throws ClientNotFoundException {
        clientServicePort.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("A client with this CPF was not found"));

        clientServicePort.delete(cpf);
    }
}