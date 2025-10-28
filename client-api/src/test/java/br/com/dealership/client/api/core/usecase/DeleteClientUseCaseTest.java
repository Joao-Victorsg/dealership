package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @InjectMocks
    private DeleteClientUseCase deleteClientUseCase;

    @Test
    void shouldDeleteClient() {
        final var cpf = "123";
        final var clientModel = Instancio.create(ClientModel.class);

        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.of(clientModel));
        doNothing().when(clientServicePort).delete(cpf);

        assertDoesNotThrow(() -> deleteClientUseCase.execute(cpf));
    }

    @Test
    void shouldThrowClientNotFoundException() {
        final var cpf = "123";

        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> deleteClientUseCase.execute(cpf));
    }
}
