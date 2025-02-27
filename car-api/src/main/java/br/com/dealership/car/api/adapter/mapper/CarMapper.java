package br.com.dealership.car.api.adapter.mapper;

import br.com.dealership.car.api.adapter.in.dto.request.CarDtoRequest;
import br.com.dealership.car.api.adapter.in.dto.response.CarDtoResponse;
import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import br.com.dealership.car.api.core.domain.CarModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Component
public class CarMapper{

    public CarModel toCarModel(CarDtoRequest carDtoRequest){
        return CarModel.builder()
                .vin(carDtoRequest.vin())
                .manufacturer(carDtoRequest.manufacturer())
                .model(carDtoRequest.model())
                .modelYear(carDtoRequest.modelYear())
                .color(carDtoRequest.color())
                .value(carDtoRequest.value())
                .registrationDate(LocalDateTime.now())
                .build();
    }

    public CarModel toCarModel(CarEntity carEntity){
        return CarModel.builder()
                .id(UUID.fromString(carEntity.getId()))
                .vin(carEntity.getVin())
                .model(carEntity.getModel())
                .modelYear(carEntity.getModelYear())
                .manufacturer(carEntity.getManufacturer())
                .color(carEntity.getColor())
                .value(carEntity.getValue())
                .registrationDate(carEntity.getRegistrationDate())
                .build();
    }

    public CarDtoResponse toCarDtoResponse(CarModel carModel){
        return CarDtoResponse.builder()
                .id(carModel.id())
                .vin(carModel.vin())
                .manufacturer(carModel.manufacturer())
                .model(carModel.model())
                .modelYear(carModel.modelYear())
                .color(carModel.color())
                .value(carModel.value())
                .registrationDate(carModel.registrationDate())
                .build();
    }

    public CarEntity toEntity(CarModel carModel){
        return CarEntity.builder()
                .vin(carModel.vin())
                .model(carModel.model())
                .modelYear(carModel.modelYear())
                .manufacturer(carModel.manufacturer())
                .color(carModel.color())
                .value(carModel.value())
                .registrationDate(carModel.registrationDate())
                .build();
    }
}