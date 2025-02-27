package br.com.dealership.client.api.core.usecase.port;

import br.com.dealership.client.api.core.domain.AddressModel;

public interface AddressServicePort {

    AddressModel search(AddressModel addressModel);
}