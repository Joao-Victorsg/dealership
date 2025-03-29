package br.com.dealership.api_client.core.usecase.port;

import br.com.dealership.api_client.core.domain.AddressModel;

public interface AddressServicePort {

    AddressModel search(AddressModel addressModel);
}