package br.com.dealership.client.api.core.usecase.port;

import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.domain.ClientModel;
import br.com.dealership.client.api.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.client.api.core.exceptions.ClientNotFoundException;
import br.com.dealership.client.api.core.exceptions.EmailAlreadyInUseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientServicePort {
    ClientModel findByCpf(final String cpf) throws ClientNotFoundException;
    Page<ClientModel> getAll(final String city, final String state, final Pageable pageable);
    ClientModel create(final ClientModel clientModel) throws ClientAlreadyExistsException, EmailAlreadyInUseException;
    void delete(final String cpf) throws ClientNotFoundException;
    ClientModel update(final String cpf, final AddressModel addressModel) throws ClientNotFoundException;
}