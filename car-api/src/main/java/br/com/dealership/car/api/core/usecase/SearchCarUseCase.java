package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SearchCarUseCase {

    private final CarServicePort carServicePort;

    public CarModel execute(String vin) throws CarNotFoundException{
        return carServicePort.findByVin(vin);
    }

    public Page<CarModel> execute(Pageable pageable, SearchFilter searchFilters){
        return carServicePort.searchAll(searchFilters,pageable);
    }
}