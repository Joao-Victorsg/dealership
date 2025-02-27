package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class SearchCarUseCase {

    private final CarServicePort carServicePort;

    public CarModel execute(String vin) throws CarNotFoundException{
        return carServicePort.findByVin(vin);
    }

    public Page<CarModel> execute(Pageable pageable, BigDecimal initValue, BigDecimal finalValue, String year, String model, String manufacturer){
        return carServicePort.getAll(initValue,finalValue,year,model,manufacturer,pageable);
    }
}