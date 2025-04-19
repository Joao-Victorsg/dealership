package br.com.dealership.sales_api.adapter.mapper;

import br.com.dealership.sales_api.adapter.out.database.entity.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarEntity toEntity(String vin){
        return CarEntity.builder()
                .vin(vin)
                .build();
    }
}