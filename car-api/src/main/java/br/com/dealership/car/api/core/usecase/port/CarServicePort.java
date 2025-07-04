package br.com.dealership.car.api.core.usecase.port;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;

public interface CarServicePort {

    CarModel findByVin(String vin) throws CarNotFoundException;

    Page<CarModel> searchAll(final SearchFilter searchFilter, final Pageable pageable);

    CarModel create(final CarModel carModel) throws CarAlreadyExistsException;

    void delete(final String vin) throws CarNotFoundException;

    CarModel update(final String vin, final String color, final BigDecimal value,
            final String modelYear) throws CarNotFoundException;

    List<String> getDistinctManufacturers();

    List<String> getDistinctModels();

    List<String> getModelsByManufacturer(String manufacturer);
}
