package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCarUseCaseTest {

    @Mock
    private CarServicePort carServicePort;

    @InjectMocks
    private UpdateCarUseCase updateCarUseCase;

    @Test
    void shouldUpdateWithSuccess() throws CarNotFoundException {
        final var vin = "123";
        final var color = "blue";
        final var value = BigDecimal.TEN;
        final var carModel = Instancio.create(CarModel.class);

        when(carServicePort.update(vin,color,value)).thenReturn(carModel);

        final var resultado = assertDoesNotThrow(() -> updateCarUseCase.execute(vin,color,value));

        assertNotNull(resultado);
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenUpdating() throws CarNotFoundException {
        final var vin = "123";
        final var color = "blue";
        final var value = BigDecimal.TEN;

        doThrow(CarNotFoundException.class).when(carServicePort).update(vin,color,value);

        assertThrows(CarNotFoundException.class,() -> updateCarUseCase.execute(vin,color,value));
    }

}