package br.com.dealership.client.api.adapter.out.database.service;

import br.com.dealership.client.api.adapter.mapper.AddressMapper;
import br.com.dealership.client.api.adapter.mapper.ClientMapper;
import br.com.dealership.client.api.adapter.out.database.entity.ClientEntity;
import br.com.dealership.client.api.adapter.out.database.repository.ClientRepository;
import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
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
    void shouldFindByCpf() {
        final var cpf = "12345678900";
        final var clientEntity = ClientEntity.builder().cpf(cpf).build();
        final var clientModel = ClientModel.builder().cpf(cpf).build();

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(clientEntity));
        when(clientMapper.toModel(clientEntity)).thenReturn(clientModel);

        final var result = clientService.findByCpf(cpf);

        assertEquals(Optional.of(clientModel), result);
    }

    @Test
    void shouldReturnEmptyWhenCpfNotFound() {
        final var cpf = "12345678900";

        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        final var result = clientService.findByCpf(cpf);

        assertEquals(Optional.empty(), result);
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
    void shouldCreateNewClient() {
        final var clientModel = ClientModel.builder().cpf("12345678900").build();
        final var clientEntity = ClientEntity.builder().cpf("12345678900").build();

        when(clientMapper.toEntity(clientModel)).thenReturn(clientEntity);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toModel(clientEntity)).thenReturn(clientModel);

        final var result = clientService.create(clientModel);

        assertEquals(clientModel, result);
    }

    @Test
    void shouldCheckIfEmailExists() {
        final var email = "email@email.com";

        when(clientRepository.existsByEmail(email)).thenReturn(true);

        final var result = clientService.existsByEmail(email);

        assertEquals(true, result);
        verify(clientRepository).existsByEmail(email);
    }

    @Test
    void shouldDeleteClientByCpf(){
        final var cpf = "12345678900";

        assertDoesNotThrow(() -> clientService.delete(cpf));
        verify(clientRepository).deleteByCpf(cpf);
    }

    @Test
    void shouldUpdateClientAddress() {
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
    void shouldReturnFalseWhenEmailDoesNotExist() {
        final var email = "notfound@email.com";

        when(clientRepository.existsByEmail(email)).thenReturn(false);

        final var result = clientService.existsByEmail(email);

        assertEquals(false, result);
        verify(clientRepository).existsByEmail(email);
    }
}