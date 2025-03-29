package br.com.dealership.api_client.adapter.in.controller;

import br.com.dealership.api_client.adapter.in.controller.dto.request.AddressDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.request.ClientDtoUpdateRequest;
import br.com.dealership.api_client.adapter.in.controller.dto.response.AddressDtoResponse;
import br.com.dealership.api_client.adapter.in.controller.dto.response.ClientDtoResponse;
import br.com.dealership.api_client.adapter.mapper.ClientMapper;
import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.CreateClientUseCase;
import br.com.dealership.api_client.core.usecase.DeleteClientUseCase;
import br.com.dealership.api_client.core.usecase.GetClientUseCase;
import br.com.dealership.api_client.core.usecase.UpdateClientUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private CreateClientUseCase createClientUseCase;

    @Mock
    private DeleteClientUseCase deleteClientUseCase;

    @Mock
    private GetClientUseCase getClientUseCase;

    @Mock
    private UpdateClientUseCase updateClientUseCase;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientController clientController;

    @Test
    void givenValidRequestCreateTheClient() throws ClientAlreadyExistsException {
        final var clientDtoRequest = ClientDtoRequest.builder()
                .cpf("123")
                .address(AddressDtoRequest.builder()
                        .build())
                .build();

        final var clientModel = ClientModel.builder()
                .cpf(clientDtoRequest.cpf())
                .clientAddress(AddressModel.builder().build())
                .build();

        final var clientDtoResponse = ClientDtoResponse.builder()
                .cpf(clientDtoRequest.cpf())
                .address(AddressDtoResponse.builder().build())
                .build();

        when(clientMapper.toModel(clientDtoRequest)).thenReturn(clientModel);
        when(createClientUseCase.execute(clientModel)).thenReturn(clientModel);
        when(clientMapper.toDtoResponse(clientModel)).thenReturn(clientDtoResponse);

        final var response = assertDoesNotThrow(() -> clientController.createClient(clientDtoRequest));

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(clientDtoRequest.cpf(),response.getBody().getData().cpf());
    }

    @Test
    @DisplayName("Given a client request with a CPF that already exists, throw DuplicatedInfoException ")
    void givenClientRequestWithCpfThatAlreadyExistsThrowClientAlreadyExistsException() throws ClientAlreadyExistsException {
        final var clientDtoRequest = ClientDtoRequest.builder()
                .cpf("123")
                .address(AddressDtoRequest.builder()
                        .build())
                .build();

        final var clientModel = ClientModel.builder()
                .cpf(clientDtoRequest.cpf())
                .clientAddress(AddressModel.builder().build())
                .build();

        when(clientMapper.toModel(clientDtoRequest)).thenReturn(clientModel);
        doThrow(ClientAlreadyExistsException.class).when(createClientUseCase).execute(clientModel);

        assertThrows(ClientAlreadyExistsException.class, () -> clientController.createClient(clientDtoRequest));
    }

    @Test
    void shouldGetAllClients() {
        final var pageable = Pageable.ofSize(10);
        final var clientModel = ClientModel.builder().cpf("123").build();
        final var clientDtoResponse = ClientDtoResponse.builder().cpf("123").build();

        when(getClientUseCase.execute(pageable, null, null))
                .thenReturn(new PageImpl<>(List.of(clientModel)));
        when(clientMapper.toDtoResponse(clientModel)).thenReturn(clientDtoResponse);

        final var response = clientController.getAllClient(pageable, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().getContent().size());
        assertEquals("123", response.getBody().getData().getContent().getFirst().cpf());
    }

    @Test
    void shouldGetClientByCpf() throws ClientNotFoundException {
        final var cpf = "123";
        final var clientModel = ClientModel.builder().cpf(cpf).build();
        final var clientDtoResponse = ClientDtoResponse.builder().cpf(cpf).build();

        when(getClientUseCase.execute(cpf)).thenReturn(clientModel);
        when(clientMapper.toDtoResponse(clientModel)).thenReturn(clientDtoResponse);

        final var response = clientController.getClient(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cpf, response.getBody().getData().cpf());
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenGettingClient() throws ClientNotFoundException {
        var cpf = "123";

        when(getClientUseCase.execute(cpf)).thenThrow(ClientNotFoundException.class);

        assertThrows(ClientNotFoundException.class, () -> clientController.getClient(cpf));
    }

    @Test
    void shouldUpdateClient() throws ClientNotFoundException {
        final var cpf = "123";
        final var postCode = "0000";
        final var updateRequest = ClientDtoUpdateRequest.builder()
                .postCode(postCode)
                .build();
        final var updatedModel = ClientModel.builder()
                .cpf(cpf)
                .clientAddress(AddressModel.builder().postCode(postCode).build())
                .build();
        final var updatedResponse = ClientDtoResponse.builder()
                .cpf(cpf)
                .address(AddressDtoResponse.builder().postCode(postCode).build())
                .build();

        when(updateClientUseCase.execute(eq(cpf), any(AddressModel.class))).thenReturn(updatedModel);
        when(clientMapper.toDtoResponse(updatedModel)).thenReturn(updatedResponse);

        final var response = clientController.updateClient(cpf, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cpf, response.getBody().getData().cpf());
        assertEquals(updateRequest.postCode(),response.getBody().getData().address().postCode());
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenTryingToUpdateClientThatNotExists() throws ClientNotFoundException {
        var cpf = "123";
        final var updateRequest = ClientDtoUpdateRequest.builder()
                .postCode("000000")
                .build();

        doThrow(ClientNotFoundException.class).when(updateClientUseCase).execute(eq(cpf), any(AddressModel.class));

        assertThrows(ClientNotFoundException.class, () -> clientController.updateClient(cpf, updateRequest));
    }

    @Test
    void shouldDeleteClient() throws ClientNotFoundException {
        var cpf = "123";

        doNothing().when(deleteClientUseCase).execute(cpf);

        final var response = clientController.deleteClient(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Client with CPF: 123 was successfully deleted", response.getBody().getData());
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenDeletingClient() throws ClientNotFoundException {
        var cpf = "123";

        doThrow(ClientNotFoundException.class).when(deleteClientUseCase).execute(cpf);

        assertThrows(ClientNotFoundException.class, () -> clientController.deleteClient(cpf));
    }
}