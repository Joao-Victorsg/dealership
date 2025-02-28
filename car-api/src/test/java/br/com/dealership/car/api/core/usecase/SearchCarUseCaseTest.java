package br.com.dealership.car.api.core.usecase;

import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCarUseCaseTest {

    @Mock
    private CarServicePort carServicePort;
    @InjectMocks
    private SearchCarUseCase searchCarUseCase;

    @Test
    void shouldSearchSpecificCarWithSuccess() throws CarNotFoundException {
        final var vin = "123";
        final var carModel = Instancio.create(CarModel.class);

        when(carServicePort.findByVin(vin)).thenReturn(carModel);

        final var resultado = assertDoesNotThrow(() -> searchCarUseCase.execute(vin));

        assertNotNull(resultado);
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenSearchingForSpecificCar() throws CarNotFoundException {
        final var vin = "123";

        doThrow(CarNotFoundException.class).when(carServicePort).findByVin(vin);

        assertThrows(CarNotFoundException.class,() -> searchCarUseCase.execute(vin));
    }

    @Test
    void shouldSearchForAllCarsWithSuccess(){
        final var searchFilters = Instancio.create(SearchFilter.class);
        final var pageable = Pageable.ofSize(1);
        final var page = new PageImpl<>(List.of(Instancio.create(CarModel.class)));

        when(carServicePort.searchAll(searchFilters,pageable)).thenReturn(page);

        final var resultado = searchCarUseCase.execute(pageable,searchFilters);

        assertNotNull(resultado);
    }
}