package br.com.dealership.client.api.adapter.out.database.service;

import br.com.dealership.client.api.adapter.mapper.AddressMapper;
import br.com.dealership.client.api.adapter.mapper.ClientMapper;
import br.com.dealership.client.api.adapter.out.database.repository.ClientRepository;
import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.usecase.port.ClientServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.dealership.client.api.adapter.out.database.repository.specification.ClientSpecificationsFactory.hasCity;
import static br.com.dealership.client.api.adapter.out.database.repository.specification.ClientSpecificationsFactory.hasState;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AddressMapper addressMapper;

    @Override
    public Optional<ClientModel> findByCpf(String cpf){
        return clientRepository.findByCpf(cpf)
                .map(clientMapper::toModel);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
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
    public ClientModel create(final ClientModel clientModel){
        final var entity = clientMapper.toEntity(clientModel);

        final var savedEntity = clientRepository.save(entity);

        return clientMapper.toModel(savedEntity);
    }

    @Transactional
    @Override
    public void delete(final String cpf) {
        clientRepository.deleteByCpf(cpf);
    }

    @Transactional
    @Override
    public ClientModel update(String cpf, AddressModel newAddressModel) {
        final var client = clientRepository.findByCpf(cpf).orElseThrow();

        final var newAddressEntity = addressMapper.toEntity(newAddressModel);

        final var updatedClientToSave = client.toBuilder()
                .address(newAddressEntity)
                .build();

        final var updatedClient = clientRepository.save(updatedClientToSave);

        return clientMapper.toModel(updatedClient);
    }
}