package br.com.dealership.api_client.adapter.out.database.service;

import br.com.dealership.api_client.adapter.mapper.AddressMapper;
import br.com.dealership.api_client.adapter.mapper.ClientMapper;
import br.com.dealership.api_client.adapter.out.database.repository.ClientRepository;
import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import br.com.dealership.api_client.core.usecase.port.ClientServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static br.com.dealership.api_client.adapter.out.database.repository.specification.ClientSpecificationsFactory.hasCity;
import static br.com.dealership.api_client.adapter.out.database.repository.specification.ClientSpecificationsFactory.hasState;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AddressMapper addressMapper;

    @Override
    public ClientModel findByCpf(String cpf) throws ClientNotFoundException {
        final var clientEntity = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("There isn't a client with this CPF"));

        return clientMapper.toModel(clientEntity);
    }

    @Override
    public Page<ClientModel> getAll(String city, String state, Pageable pageable) {

        final var specification = Specification.where(hasCity(city)).and(hasState(state));

        final var clientsEntities = clientRepository.findAll(specification,pageable);

        final var clientsModel = clientsEntities.stream()
                .map(clientMapper::toModel)
                .toList();

        return new PageImpl<>(clientsModel,clientsEntities.getPageable(),clientsModel.size());
    }


    @Override
    public ClientModel create(final ClientModel clientModel) throws ClientAlreadyExistsException {
        if(clientRepository.findByCpf(clientModel.cpf()).isPresent())
            throw new ClientAlreadyExistsException("A client with this CPF already exists");

        final var entity = clientMapper.toEntity(clientModel);

        final var savedEntity = clientRepository.save(entity);

        return clientMapper.toModel(savedEntity);
    }

    @Transactional
    @Override
    public void delete(final String cpf) throws ClientNotFoundException {
        clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("A client with this CPF was not found"));

        clientRepository.deleteByCpf(cpf);
    }

    @Transactional
    @Override
    public ClientModel update(String cpf, AddressModel newAddressModel) throws ClientNotFoundException {
        final var client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("A client with this CPF was not found"));

        final var newAddressEntity = addressMapper.toEntity(newAddressModel);

        final var updatedClientToSave = client.toBuilder()
                .address(newAddressEntity)
                .build();

        final var updatedClient = clientRepository.save(updatedClientToSave);

        return clientMapper.toModel(updatedClient);
    }
}