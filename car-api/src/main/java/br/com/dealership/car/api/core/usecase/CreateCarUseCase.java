package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import br.com.dealership.car.api.core.usecase.port.SendCreationEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCarUseCase {

    private final CarServicePort carServicePort;
    private final SendCreationEventPort sendCreationEventPort;

    public CarModel execute(CarModel carModel) throws CarAlreadyExistsException {
        final var createdCarModel = carServicePort.create(carModel);

        sendCreationEventPort.sendCreationEvent(createdCarModel.vin());

        return  createdCarModel;
    }
}