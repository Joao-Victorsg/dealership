package br.com.dealership.car.api.core.usecase;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCarUseCase {

    private final CarServicePort carServicePort;

    public CarModel execute(String vin, String color, BigDecimal value, String modelYear)
            throws CarNotFoundException {
        return carServicePort.update(vin, color, value, modelYear);
    }
}
