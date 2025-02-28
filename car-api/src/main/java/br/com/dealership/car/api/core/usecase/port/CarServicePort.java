package br.com.dealership.car.api.core.usecase.port;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CarServicePort {

    CarModel findByVin(String vin) throws CarNotFoundException;

    Page<CarModel> searchAll(final SearchFilter searchFilter, final Pageable pageable);

    CarModel create(final CarModel carModel) throws CarAlreadyExistsException;

    void delete(final String vin) throws  CarNotFoundException;

    CarModel update(final String vin, final String color, final BigDecimal value) throws CarNotFoundException;
}