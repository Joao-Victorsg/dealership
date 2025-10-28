package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @Mock
    private AddressServicePort addressServicePort;

    @InjectMocks
    private UpdateClientUseCase updateClientUseCase;

    @Test
    void shouldUpdateClient() {
        final var cpf = "123";
        final var addressModel = Instancio.create(AddressModel.class);
        final var clientModel = Instancio.create(ClientModel.class);

        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.of(clientModel));
        when(addressServicePort.search(addressModel)).thenReturn(addressModel);
        when(clientServicePort.update(cpf, addressModel)).thenReturn(clientModel);

        final var result = assertDoesNotThrow(() -> updateClientUseCase.execute(cpf, addressModel));

        assertNotNull(result);
    }

    @Test
    void shouldThrowClientNotFoundException() {
        final var cpf = "123";
        final var addressModel = Instancio.create(AddressModel.class);

        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> updateClientUseCase.execute(cpf, addressModel));
    }
}