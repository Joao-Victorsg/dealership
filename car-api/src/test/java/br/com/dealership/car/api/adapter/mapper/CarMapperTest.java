package br.com.dealership.car.api.adapter.mapper;

import br.com.dealership.car.api.adapter.in.dto.request.CarDtoRequest;
import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import br.com.dealership.car.api.core.domain.CarModel;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CarMapperTest {

    private final CarMapper carMapper = new CarMapper();

    @Test
    void shouldMapCarDtoRequestToCarModel() {
        final var carDtoRequest = Instancio.create(CarDtoRequest.class);

        final var carModel = carMapper.toCarModel(carDtoRequest);

        assertThat(carModel).isNotNull();
        assertThat(carModel.vin()).isEqualTo(carDtoRequest.vin());
        assertThat(carModel.manufacturer()).isEqualTo(carDtoRequest.manufacturer());
        assertThat(carModel.model()).isEqualTo(carDtoRequest.model());
        assertThat(carModel.modelYear()).isEqualTo(carDtoRequest.modelYear());
        assertThat(carModel.color()).isEqualTo(carDtoRequest.color());
        assertThat(carModel.value()).isEqualTo(carDtoRequest.value());
        assertThat(carModel.registrationDate()).isNotNull();
    }

    @Test
    void shouldMapCarEntityToCarModel() {
        final var carEntity = Instancio.of(CarEntity.class)
                .set(Select.field(CarEntity::getId),UUID.randomUUID().toString())
                .create();

        final var carModel = carMapper.toCarModel(carEntity);

        assertThat(carModel).isNotNull();
        assertThat(carModel.id()).isEqualTo(UUID.fromString(carEntity.getId()));
        assertThat(carModel.vin()).isEqualTo(carEntity.getVin());
        assertThat(carModel.manufacturer()).isEqualTo(carEntity.getManufacturer());
        assertThat(carModel.model()).isEqualTo(carEntity.getModel());
        assertThat(carModel.modelYear()).isEqualTo(carEntity.getModelYear());
        assertThat(carModel.color()).isEqualTo(carEntity.getColor());
        assertThat(carModel.value()).isEqualTo(carEntity.getValue());
        assertThat(carModel.registrationDate()).isEqualTo(carEntity.getRegistrationDate());
    }

    @Test
    void shouldMapCarModelToCarDtoResponse() {
        final var carModel = Instancio.create(CarModel.class);

        final var response = carMapper.toCarDtoResponse(carModel);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(carModel.id());
        assertThat(response.vin()).isEqualTo(carModel.vin());
        assertThat(response.manufacturer()).isEqualTo(carModel.manufacturer());
        assertThat(response.model()).isEqualTo(carModel.model());
        assertThat(response.modelYear()).isEqualTo(carModel.modelYear());
        assertThat(response.color()).isEqualTo(carModel.color());
        assertThat(response.value()).isEqualTo(carModel.value());
        assertThat(response.registrationDate()).isEqualTo(carModel.registrationDate());
    }

    @Test
    void shouldMapCarModelToCarEntity() {
        final var carModel = Instancio.create(CarModel.class);

        final var carEntity = carMapper.toEntity(carModel);

        assertThat(carEntity).isNotNull();
        assertThat(carEntity.getVin()).isEqualTo(carModel.vin());
        assertThat(carEntity.getManufacturer()).isEqualTo(carModel.manufacturer());
        assertThat(carEntity.getModel()).isEqualTo(carModel.model());
        assertThat(carEntity.getModelYear()).isEqualTo(carModel.modelYear());
        assertThat(carEntity.getColor()).isEqualTo(carModel.color());
        assertThat(carEntity.getValue()).isEqualTo(carModel.value());
        assertThat(carEntity.getRegistrationDate()).isEqualTo(carModel.registrationDate());
    }
}
