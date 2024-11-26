package br.com.dealership.api_client.adapter.out.gateway;


import br.com.dealership.api_client.adapter.out.gateway.dto.AddressDtoGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "via-cep-api", url = "${via-cep.url}")
public interface SearchAddressGateway {

    @CircuitBreaker(name="SearchAddressGatewaybyPostCode", fallbackMethod = "byPostCodeFallback")
    @GetMapping(value = "{postCode}/json", produces = "application/json")
    AddressDtoGateway byPostCode(@PathVariable("postCode") String postCode);

    default AddressDtoGateway byPostCodeFallback(String postCode, Exception ex){
        return AddressDtoGateway.builder()
                .postCode(postCode)
                .isAddressSearched(false)
                .build();
    }
}