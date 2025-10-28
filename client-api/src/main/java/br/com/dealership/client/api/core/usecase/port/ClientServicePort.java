package br.com.dealership.client.api.core.usecase.port;

import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientServicePort {
    Optional<ClientModel> findByCpf(final String cpf);
    boolean existsByEmail(final String email);
    Page<ClientModel> getAll(final String city, final String state, final Pageable pageable);
    ClientModel create(final ClientModel clientModel);
    void delete(final String cpf);
    ClientModel update(final String cpf, final AddressModel addressModel);
}