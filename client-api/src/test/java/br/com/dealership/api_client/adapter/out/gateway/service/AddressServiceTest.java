package br.com.dealership.api_client.adapter.out.gateway.service;

import br.com.dealership.api_client.adapter.mapper.AddressMapper;
import br.com.dealership.api_client.adapter.out.gateway.SearchAddressGateway;
import br.com.dealership.api_client.adapter.out.gateway.dto.AddressDtoGateway;
import br.com.dealership.api_client.core.domain.AddressModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private SearchAddressGateway searchAddressGateway;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @Test
    void shouldSearchAndMapAddressSuccessfully() {
        final var postCode = "123";
        final var addressModel = AddressModel.builder().postCode(postCode).build();
        final var searchedAddress = AddressDtoGateway.builder().postCode(postCode).build();
        final var resultModel = AddressModel.builder().postCode(postCode).build();

        when(searchAddressGateway.byPostCode(addressModel.postCode())).thenReturn(searchedAddress);
        when(addressMapper.toModel(addressModel, searchedAddress)).thenReturn(resultModel);

        final var result = addressService.search(addressModel);

        assertNotNull(result);
        assertEquals(postCode, result.postCode());
    }
}