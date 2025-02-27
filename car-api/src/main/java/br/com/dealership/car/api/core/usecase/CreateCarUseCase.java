package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCarUseCase {

    private final CarServicePort carServicePort;

    public CarModel execute(CarModel carModel) throws CarAlreadyExistsException {
        return carServicePort.create(carModel);
    }
}