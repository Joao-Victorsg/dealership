package br.com.dealership.api_client.core.usecase.port;

import br.com.dealership.api_client.core.domain.AddressModel;
import br.com.dealership.api_client.core.domain.ClientModel;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientServicePort {
    ClientModel findByCpf(final String cpf) throws ClientNotFoundException;
    Page<ClientModel> getAll(final String city, final String state, final Pageable pageable);
    ClientModel create(final ClientModel clientModel) throws ClientAlreadyExistsException;
    void delete(final String cpf) throws ClientNotFoundException;
    ClientModel update(final String cpf, final AddressModel addressModel) throws ClientNotFoundException;
}