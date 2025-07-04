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
class SearchManufacturersUseCaseTest {

    @Mock
    private CarServicePort carServicePort;

    @InjectMocks
    private SearchManufacturersUseCase searchManufacturersUseCase;

    @Test
    @DisplayName("Should return distinct manufacturers when service provides data")
    void shouldReturnDistinctManufacturers() {
        final var manufacturers = List.of("ManufacturerA", "ManufacturerB", "ManufacturerC");
        when(carServicePort.getDistinctManufacturers()).thenReturn(manufacturers);

        final var result = searchManufacturersUseCase.execute();

        assertEquals(manufacturers, result);
    }

    @Test
    @DisplayName("Should return empty list when service provides no manufacturers")
    void shouldReturnEmptyListWhenNoManufacturers() {
        when(carServicePort.getDistinctManufacturers()).thenReturn(Collections.emptyList());

        final var result = searchManufacturersUseCase.execute();

        assertEquals(Collections.emptyList(), result);
    }
}