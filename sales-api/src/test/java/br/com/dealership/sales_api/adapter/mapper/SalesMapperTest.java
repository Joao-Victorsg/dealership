package br.com.dealership.sales_api.adapter.mapper;

import br.com.dealership.sales_api.adapter.in.controller.dto.request.SalesDtoRequest;
import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import br.com.dealership.sales_api.core.domain.SalesModel;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SalesMapperTest {

    private final CarMapper carMapper = new CarMapper();
    private final ClientMapper clientMapper = new ClientMapper();
    private final SalesMapper salesMapper = new SalesMapper(carMapper, clientMapper);

    @Test
    void shouldMapSalesDtoRequestToSalesModel() {
        final var salesDtoRequest = Instancio.create(SalesDtoRequest.class);

        final var salesModel = salesMapper.toModel(salesDtoRequest);

        assertThat(salesModel).isNotNull();
        assertThat(salesModel.cpf()).isEqualTo(salesDtoRequest.cpf());
        assertThat(salesModel.vin()).isEqualTo(salesDtoRequest.vin());
        assertThat(salesModel.registrationDate()).isNotNull();
    }

    @Test
    void shouldMapSalesModelToSalesDtoResponse() {
        final var salesModel = Instancio.create(SalesModel.class);

        final var salesDtoResponse = salesMapper.toDto(salesModel);

        assertThat(salesDtoResponse).isNotNull();
        assertThat(salesDtoResponse.id()).isEqualTo(salesModel.id());
        assertThat(salesDtoResponse.cpf()).isEqualTo(salesModel.cpf());
        assertThat(salesDtoResponse.vin()).isEqualTo(salesModel.vin());
        assertThat(salesDtoResponse.registrationDate()).isEqualTo(salesModel.registrationDate());
    }

    @Test
    void shouldMapSalesEntityToSalesModel() {
        final var salesEntity = Instancio.of(SalesEntity.class)
                .set(Select.field(SalesEntity::getId), UUID.randomUUID())
                .create();

        final var salesModel = salesMapper.toModel(salesEntity);

        assertThat(salesModel).isNotNull();
        assertThat(salesModel.id()).isEqualTo(salesEntity.getId());
        assertThat(salesModel.cpf()).isEqualTo(salesEntity.getClient().getCpf());
        assertThat(salesModel.vin()).isEqualTo(salesEntity.getCar().getVin());
        assertThat(salesModel.registrationDate()).isEqualTo(salesEntity.getRegistrationDate());
    }

    @Test
    void shouldMapSalesModelToSalesEntity() {
        final var salesModel = Instancio.create(SalesModel.class);

        final var salesEntity = salesMapper.toEntity(salesModel);

        assertThat(salesEntity).isNotNull();
        assertThat(salesEntity.getCar().getVin()).isEqualTo(salesModel.vin());
        assertThat(salesEntity.getClient().getCpf()).isEqualTo(salesModel.cpf());
        assertThat(salesEntity.getRegistrationDate()).isEqualTo(salesModel.registrationDate());
    }
}