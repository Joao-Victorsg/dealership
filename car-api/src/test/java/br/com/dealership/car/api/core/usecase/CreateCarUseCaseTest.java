package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import br.com.dealership.car.api.core.usecase.port.SendCreationEventPort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCarUseCaseTest {

    @Mock
    private CarServicePort carServicePort;
    @Mock
    private SendCreationEventPort sendCreationEventPort;
    @InjectMocks
    private CreateCarUseCase createCarUseCase;

    @Test
    void shouldCreateCarWithSuccess() throws CarAlreadyExistsException {
        final var carModel = Instancio.create(CarModel.class);
        when(carServicePort.create(carModel)).thenReturn(carModel);
        doNothing().when(sendCreationEventPort).sendCreationEvent(carModel.vin());

        final var resultado = assertDoesNotThrow(() -> createCarUseCase.execute(carModel));

        assertNotNull(resultado);
    }

    @Test
    void shouldThrowCarAlreadyExistsException() throws CarAlreadyExistsException {
        final var carModel = Instancio.create(CarModel.class);

        doThrow(CarAlreadyExistsException.class).when(carServicePort).create(carModel);

        assertThrows(CarAlreadyExistsException.class,() -> createCarUseCase.execute(carModel));
        verifyNoInteractions(sendCreationEventPort);
    }
}