package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SearchModelsUseCase {

    private final CarServicePort carServicePort;

    public List<String> execute() {
        return carServicePort.getDistinctModels();
    }

    public List<String> executeByManufacturer(String manufacturer) {
        return carServicePort.getModelsByManufacturer(manufacturer);
    }
}
