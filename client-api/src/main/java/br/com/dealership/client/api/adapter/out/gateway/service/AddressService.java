package br.com.dealership.client.api.adapter.out.gateway.service;

import br.com.dealership.client.api.adapter.mapper.AddressMapper;
import br.com.dealership.client.api.adapter.out.gateway.SearchAddressGateway;
import br.com.dealership.client.api.core.domain.AddressModel;
import br.com.dealership.client.api.core.usecase.port.AddressServicePort;
import br.com.dealership.client.api.utils.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService implements AddressServicePort {

    private final SearchAddressGateway searchAddressGateway;
    private final AddressMapper addressMapper;

    @Override
    public AddressModel search(AddressModel addressModel) {
        Logger.info("Searching address with postcode: " + addressModel.postCode());

        final var searchedAddress = searchAddressGateway.byPostCode(addressModel.postCode());

        Logger.info("Address searched with sucess to postcode: " +  addressModel.postCode());

        return addressMapper.toModel(addressModel,searchedAddress);
    }
}