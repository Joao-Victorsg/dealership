package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteClientUseCase {

    private final ClientServicePort clientServicePort;

    public void execute(String cpf) throws ClientNotFoundException {
        clientServicePort.delete(cpf);
    }
}