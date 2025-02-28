package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DeleteCarUseCaseTest {

    @Mock
    private CarServicePort carServicePort;
    @InjectMocks
    private DeleteCarUseCase deleteCarUseCase;

    @Test
    void shouldDeleteCarWithSuccess() throws CarNotFoundException {
        final var vin = "123";
        doNothing().when(carServicePort).delete(vin);

        assertDoesNotThrow(() -> deleteCarUseCase.execute(vin));
    }

    @Test
    void shouldThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123";
        doThrow(CarNotFoundException.class).when(carServicePort).delete(vin);

        assertThrows(CarNotFoundException.class, () -> deleteCarUseCase.execute(vin));
    }

}