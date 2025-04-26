package br.com.dealership.sales_api.adapter.mapper;

import br.com.dealership.sales_api.adapter.out.database.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientEntity toEntity(String cpf){
        return ClientEntity.builder()
                .cpf(cpf)
                .build();
    }
}