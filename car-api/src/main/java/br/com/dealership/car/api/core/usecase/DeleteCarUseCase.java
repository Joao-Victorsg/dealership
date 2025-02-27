package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteCarUseCase {

    private final CarServicePort carServicePort;

    public void execute(String vin) throws CarNotFoundException {
        carServicePort.delete(vin);
    }
}
