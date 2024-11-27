package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DeleteClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @InjectMocks
    private DeleteClientUseCase deleteClientUseCase;

    @Test
    void shouldDeleteClient() throws ClientNotFoundException {
        final var cpf = "123";

        doNothing().when(clientServicePort).delete(cpf);

        assertDoesNotThrow(() -> deleteClientUseCase.execute(cpf));
    }

    @Test
    void shouldThrowClientNotFoundException() throws ClientNotFoundException {
        final var cpf = "123";

        doThrow(ClientNotFoundException.class).when(clientServicePort).delete(cpf);

        assertThrows(ClientNotFoundException.class, () -> deleteClientUseCase.execute(cpf));
    }
}
