package br.com.dealership.client.api.core.usecase;

import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.client.api.core.exceptions.EmailAlreadyInUseException;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import br.com.dealership.client.api.core.usecase.port.SendCreationEventPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @Mock
    private AddressServicePort addressServicePort;

    @Mock
    private SendCreationEventPort sendCreationEventPort;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    @Test
    void shouldExecuteWithSuccess() {
        final var addressModel = Instancio.create(AddressModel.class);
        final var clientModel = Instancio.create(ClientModel.class);

        when(clientServicePort.findByCpf(clientModel.cpf())).thenReturn(Optional.empty());
        when(clientServicePort.existsByEmail(clientModel.email())).thenReturn(false);
        when(addressServicePort.search(clientModel.clientAddress())).thenReturn(addressModel);
        when(clientServicePort.create(any(ClientModel.class))).thenReturn(clientModel);
        doNothing().when(sendCreationEventPort).sendCreationEvent(clientModel.cpf());

        final var response = assertDoesNotThrow(() -> createClientUseCase.execute(clientModel));

        assertNotNull(response);
    }

    @Test
    void shouldThrowClientAlreadyExistsException() {
        final var clientModel = Instancio.create(ClientModel.class);

        when(clientServicePort.findByCpf(clientModel.cpf())).thenReturn(Optional.of(clientModel));

        assertThrows(ClientAlreadyExistsException.class, () -> createClientUseCase.execute(clientModel));
    }

    @Test
    void shouldThrowEmailAlreadyInUseException() {
        final var clientModel = Instancio.create(ClientModel.class);

        when(clientServicePort.findByCpf(clientModel.cpf())).thenReturn(Optional.empty());
        when(clientServicePort.existsByEmail(clientModel.email())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> createClientUseCase.execute(clientModel));
    }
}
