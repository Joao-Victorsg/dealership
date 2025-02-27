package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UpdateCarUseCase {

    private final CarServicePort carServicePort;

    public CarModel execute(String vin, String color, BigDecimal value) throws CarNotFoundException {
        return carServicePort.update(vin,color,value);
    }
}