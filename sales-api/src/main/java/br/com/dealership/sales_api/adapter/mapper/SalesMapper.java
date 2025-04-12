package br.com.dealership.sales_api.adapter.mapper;

import br.com.dealership.sales_api.adapter.in.controller.dto.request.SalesDtoRequest;
import br.com.dealership.sales_api.adapter.in.controller.dto.response.SalesDtoResponse;
import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import br.com.dealership.sales_api.core.domain.SalesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class SalesMapper {

    private final CarMapper carMapper;
    private final ClientMapper clientMapper;

    public SalesModel toModel(SalesDtoRequest salesDtoRequest){
        return SalesModel.builder()
                .cpf(salesDtoRequest.cpf())
                .vin(salesDtoRequest.vin())
                .registrationDate(LocalDateTime.now())
                .build();
    }

    public SalesDtoResponse toDto(SalesModel salesModel){
        return SalesDtoResponse.builder()
                .id(salesModel.id())
                .cpf(salesModel.cpf())
                .vin(salesModel.vin())
                .registrationDate(salesModel.registrationDate())
                .build();
    }

    public SalesModel toModel(SalesEntity salesEntity){
        return SalesModel.builder()
                .id(salesEntity.getId())
                .cpf(salesEntity.getClient().getCpf())
                .vin(salesEntity.getCar().getVin())
                .registrationDate(salesEntity.getRegistrationDate())
                .build();
    }

    public SalesEntity toEntity(SalesModel salesModel){
        final var carEntity = carMapper.toCarEntity(salesModel.vin());
        final var clientEntity = clientMapper.toClientEntity(salesModel.cpf());

        return SalesEntity.builder()
                .car(carEntity)
                .client(clientEntity)
                .registrationDate(salesModel.registrationDate())
                .build();
    }
}