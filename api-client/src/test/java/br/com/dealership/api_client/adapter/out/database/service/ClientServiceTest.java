package br.com.dealership.api_client.adapter.out.database.service;

import br.com.dealership.api_client.adapter.mapper.AddressMapper;
import br.com.dealership.api_client.adapter.mapper.ClientMapper;
import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import br.com.dealership.api_client.adapter.out.database.repository.ClientRepository;
import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldFindByCpf() throws ClientNotFoundException {
        final var cpf = "12345678900";
        final var clientEntity = ClientEntity.builder().cpf(cpf).build();
        final var clientModel = ClientModel.builder().cpf(cpf).build();

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));
        when(clientMapper.toModel(clientEntity)).thenReturn(clientModel);

        final var result = clientService.findByCpf(cpf);

        assertEquals(clientModel, result);
    }

    @Test
    void shouldThrowExceptionWhenCpfNotFound() {
        final var cpf = "12345678900";

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.findByCpf(cpf));
        verify(clientRepository).findByCpf(cpf);
    }

    @Test
    void shouldGetAllClients() {
        final var pageable = Pageable.unpaged();
        final var clientEntity = ClientEntity.builder().build();
        final var clientModel = ClientModel.builder().build();
        final var clientEntities = new PageImpl<>(List.of(clientEntity));

        when(clientRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(clientEntities);
        when(clientMapper.toModel(clientEntity)).thenReturn(clientModel);

        final var result = clientService.getAll(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(clientModel, result.getContent().getFirst());
    }

    @Test
    void shouldCreateNewClient() throws ClientAlreadyExistsException {
        final var clientModel = ClientModel.builder().cpf("12345678900").build();
        final var clientEntity = ClientEntity.builder().cpf("12345678900").build();

        when(clientRepository.findByCpf(clientModel.cpf())).thenReturn(Optional.empty());
        when(clientMapper.toEntity(clientModel)).thenReturn(clientEntity);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toModel(clientEntity)).thenReturn(clientModel);

        final var result = clientService.create(clientModel);

        assertEquals(clientModel, result);
    }

    @Test
    void shouldThrowExceptionWhenSavingExistingClient() {
        final var clientModel = ClientModel.builder().cpf("12345678900").build();
        final var clientEntity = ClientEntity.builder().cpf("12345678900").build();

        when(clientRepository.findByCpf(clientModel.cpf())).thenReturn(Optional.of(clientEntity));

        assertThrows(ClientAlreadyExistsException.class, () -> clientService.create(clientModel));

        verifyNoMoreInteractions(clientRepository, clientMapper);
    }

    @Test
    void shouldDeleteClientByCpf(){
        final var cpf = "12345678900";
        final var clientEntity = ClientEntity.builder().cpf(cpf).build();

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));

        assertDoesNotThrow(() -> clientService.delete(cpf));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentClient() {
        final var cpf = "12345678900";

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.delete(cpf));
    }

    @Test
    void shouldUpdateClientAddress() throws ClientNotFoundException {
        final var cpf = "12345678900";
        final var addressModel = AddressModel.builder().postCode("12345").build();
        final var clientEntity = ClientEntity.builder().cpf(cpf).build();
        final var updatedClientEntity = ClientEntity.builder().cpf(cpf).build();
        final var updatedClientModel = ClientModel.builder().cpf(cpf).build();

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));
        when(addressMapper.toEntity(addressModel)).thenReturn(clientEntity.getAddress());
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(updatedClientEntity);
        when(clientMapper.toModel(updatedClientEntity)).thenReturn(updatedClientModel);

        final var result = clientService.update(cpf, addressModel);

        assertEquals(updatedClientModel, result);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentClient() {
        final var cpf = "12345678900";
        final var addressModel = AddressModel.builder().build();

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.update(cpf, addressModel));
        verify(clientRepository).findByCpf(cpf);
    }
}