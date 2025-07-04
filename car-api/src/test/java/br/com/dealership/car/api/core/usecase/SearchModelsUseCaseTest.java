package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchModelsUseCaseTest {

    @Mock
    private CarServicePort carServicePort;

    @InjectMocks
    private SearchModelsUseCase searchModelsUseCase;


    @Test
    @DisplayName("Should return distinct models when service provides data")
    void shouldReturnDistinctModels() {
        final var models = List.of("ModelA", "ModelB", "ModelC");
        when(carServicePort.getDistinctModels()).thenReturn(models);

        final var result = searchModelsUseCase.execute();

        assertEquals(models, result);
    }

    @Test
    @DisplayName("Should return empty list when service provides no models")
    void shouldReturnEmptyListWhenNoModels() {
        when(carServicePort.getDistinctModels()).thenReturn(Collections.emptyList());

        final var result = searchModelsUseCase.execute();

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    @DisplayName("Should return models by manufacturer when service provides data")
    void shouldReturnModelsByManufacturer() {
        final var manufacturer = "ManufacturerA";
        final var models = List.of("ModelX", "ModelY");
        when(carServicePort.getModelsByManufacturer(manufacturer)).thenReturn(models);

        final var result = searchModelsUseCase.executeByManufacturer(manufacturer);

        assertEquals(models, result);
    }

    @Test
    @DisplayName("Should return empty list when service provides no models for manufacturer")
    void shouldReturnEmptyListForManufacturerWhenNoModels() {
        final var manufacturer = "ManufacturerB";
        when(carServicePort.getModelsByManufacturer(manufacturer)).thenReturn(Collections.emptyList());

        final var result = searchModelsUseCase.executeByManufacturer(manufacturer);

        assertEquals(Collections.emptyList(), result);
    }
}