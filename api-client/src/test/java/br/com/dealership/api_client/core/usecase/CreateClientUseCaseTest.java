package br.com.dealership.api_client.core.usecase;

import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @Mock
    private AddressServicePort addressServicePort;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    @Test
    void shouldExecuteWithSuccess() throws ClientAlreadyExistsException {
        final var addressModel = Instancio.create(AddressModel.class);
        final var clientModel = Instancio.create(ClientModel.class);

        when(addressServicePort.search(clientModel.clientAddress())).thenReturn(addressModel);
        when(clientServicePort.create(any(ClientModel.class))).thenReturn(clientModel);

        final var response = assertDoesNotThrow(() -> createClientUseCase.execute(clientModel));

        assertNotNull(response);
    }

    @Test
    void shouldThrowClientAlreadyExistsException() throws ClientAlreadyExistsException {
        final var addressModel = Instancio.create(AddressModel.class);
        final var clientModel = Instancio.create(ClientModel.class);

        when(addressServicePort.search(clientModel.clientAddress())).thenReturn(addressModel);
        doThrow(ClientAlreadyExistsException.class).when(clientServicePort).create(any(ClientModel.class));

        assertThrows(ClientAlreadyExistsException.class,() -> createClientUseCase.execute(clientModel));
    }
}
