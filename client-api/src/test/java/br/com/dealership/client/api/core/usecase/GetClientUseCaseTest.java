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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetClientUseCaseTest {

    @Mock
    private ClientServicePort clientServicePort;

    @InjectMocks
    private GetClientUseCase getClientUseCase;

    @Test
    void shouldReturnClientModelWhenCpfExists() {
        final String cpf = "12345678900";
        final ClientModel clientModel = Instancio.create(ClientModel.class);
        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.of(clientModel));

        final var response = assertDoesNotThrow(() -> getClientUseCase.execute(cpf));

        assertNotNull(response);
        assertEquals(clientModel, response);
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenCpfDoesNotExist() {
        final String cpf = "12345678900";
        when(clientServicePort.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> getClientUseCase.execute(cpf));
    }

    @Test
    void shouldReturnPageOfClientsWhenGettingAllClients() {
        final Pageable pageable = PageRequest.of(0, 10);
        final String city = "Sample City";
        final String state = "Sample State";
        final List<ClientModel> clients = List.of(Instancio.create(ClientModel.class), Instancio.create(ClientModel.class));
        final Page<ClientModel> clientPage = new PageImpl<>(clients, pageable, clients.size());
        when(clientServicePort.getAll(city, state, pageable)).thenReturn(clientPage);

        final var response = getClientUseCase.execute(pageable, city, state);

        assertNotNull(response);
        assertEquals(clients.size(), response.getTotalElements());
        assertEquals(clientPage, response);
    }

    @Test
    void shouldReturnEmptyPageWhenNoClientsExist() {
        final Pageable pageable = PageRequest.of(0, 10);
        final String city = "Nonexistent City";
        final String state = "Nonexistent State";
        final Page<ClientModel> emptyPage = Page.empty();
        when(clientServicePort.getAll(city, state, pageable)).thenReturn(emptyPage);

        final var response = getClientUseCase.execute(pageable, city, state);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(clientServicePort, times(1)).getAll(city, state, pageable);
    }
}