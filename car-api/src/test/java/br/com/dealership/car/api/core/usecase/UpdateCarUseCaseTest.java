package br.com.dealership.car.api.core.usecase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;

@ExtendWith(MockitoExtension.class)
class UpdateCarUseCaseTest {

    @Mock
    private CarServicePort carServicePort;

    @InjectMocks
    private UpdateCarUseCase updateCarUseCase;

    @Test
    void shouldUpdateCarWithSuccess() throws CarNotFoundException {
        final var vin = "123456789";
        final var color = "red";
        final var value = BigDecimal.valueOf(10000.00);
        final var modelYear = "2022";
        final var carModel = CarModel.builder().vin(vin).color(color).value(value).modelYear(modelYear).build();

        when(carServicePort.update(vin, color, value,modelYear)).thenReturn(carModel);

        final var resultado = assertDoesNotThrow(() -> updateCarUseCase.execute(vin, color, value,modelYear));

        assertNotNull(resultado);
    }

    @Test
    void shouldThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123456789";
        final var color = "red";
        final var value = BigDecimal.valueOf(10000.00);
        final var modelYear = "2023";

        when(carServicePort.update(vin, color, value, modelYear))
                .thenThrow(CarNotFoundException.class);

        assertThrows(CarNotFoundException.class,
                () -> updateCarUseCase.execute(vin, color, value, modelYear));
    }
}
