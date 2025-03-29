package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.AddressServicePort;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
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
    void shouldUpdateClient() throws ClientNotFoundException {
        final var cpf = "123";
        final var addressModel = Instancio.create(AddressModel.class);

        when(addressServicePort.search(addressModel)).thenReturn(addressModel);
        when(clientServicePort.update(cpf,addressModel)).thenReturn(Instancio.create(ClientModel.class));

        final var clientModel = assertDoesNotThrow(() -> updateClientUseCase.execute(cpf,addressModel));

        assertNotNull(clientModel);
    }

    @Test
    void shouldThrowClietNotFoundException() throws ClientNotFoundException {
        final var cpf = "123";
        final var addressModel = Instancio.create(AddressModel.class);

        when(addressServicePort.search(addressModel)).thenReturn(addressModel);
        doThrow(ClientNotFoundException.class).when(clientServicePort).update(cpf,addressModel);

        assertThrows(ClientNotFoundException.class, () -> updateClientUseCase.execute(cpf,addressModel));
    }
}