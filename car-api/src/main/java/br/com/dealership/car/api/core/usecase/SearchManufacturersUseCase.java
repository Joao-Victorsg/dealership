package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SearchManufacturersUseCase {

    private final CarServicePort carServicePort;

    public List<String> execute() {
        return carServicePort.getDistinctManufacturers();
    }
}