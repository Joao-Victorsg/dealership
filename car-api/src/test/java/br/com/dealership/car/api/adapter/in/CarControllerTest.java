package br.com.dealership.car.api.adapter.in;

import br.com.dealership.car.api.adapter.in.dto.request.CarDtoRequest;
import br.com.dealership.car.api.adapter.in.dto.request.CarDtoUpdateRequest;
import br.com.dealership.car.api.adapter.in.dto.response.CarDtoResponse;
import br.com.dealership.car.api.adapter.mapper.CarMapper;
import br.com.dealership.car.api.adapter.mapper.SearchFilterMapper;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.CreateCarUseCase;
import br.com.dealership.car.api.core.usecase.DeleteCarUseCase;
import br.com.dealership.car.api.core.usecase.SearchCarUseCase;
import br.com.dealership.car.api.core.usecase.UpdateCarUseCase;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CreateCarUseCase createCarUseCase;

    @Mock
    private DeleteCarUseCase deleteCarUseCase;

    @Mock
    private SearchCarUseCase searchCarUseCase;

    @Mock
    private UpdateCarUseCase updateCarUseCase;

    @Mock
    private CarMapper carMapper;

    @Mock
    private SearchFilterMapper searchFilterMapper;

    @InjectMocks
    private CarController carController;

    @Test
    void createCarShouldReturnCreatedResponse() throws CarAlreadyExistsException {
        final var request = Instancio.create(CarDtoRequest.class);
        final var carModel = Instancio.create(CarModel.class);
        final var response = Instancio.create(CarDtoResponse.class);

        when(carMapper.toCarModel(request)).thenReturn(carModel);
        when(createCarUseCase.execute(carModel)).thenReturn(carModel);
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(response);

        final var result = carController.createCar(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createCarShouldThrowCarAlreadyExistsException() throws CarAlreadyExistsException {
        final var request = Instancio.create(CarDtoRequest.class);
        final var model = Instancio.create(CarModel.class);

        when(carMapper.toCarModel(request)).thenReturn(model);
        doThrow(new CarAlreadyExistsException("Car already exists")).when(createCarUseCase).execute(model);

        assertThrows(CarAlreadyExistsException.class,() -> carController.createCar(request));
    }

    @Test
    void searchByVin() throws CarNotFoundException {
        final var vin = "123";
        final var carModel = Instancio.create(CarModel.class);

        when(searchCarUseCase.execute(vin)).thenReturn(carModel);

        assertDoesNotThrow(() -> carController.searchCarByVin(vin));
    }

    @Test
    void searchByVindShouldThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123";

        doThrow(CarNotFoundException.class).when(searchCarUseCase).execute(vin);

        assertThrows(CarNotFoundException.class,() -> carController.searchCarByVin(vin));
    }

    @Test
    void searchAllCarsShouldReturnPageOfCars() {
        final var pageable = Pageable.ofSize(10);
        final var carModel = Instancio.create(CarModel.class);
        final var carDtoResponse = Instancio.create(CarDtoResponse.class);
        final var searchFilter = Instancio.create(SearchFilter.class);
        final var initialValue = BigDecimal.ZERO;
        final var finalValue = BigDecimal.ZERO;
        final var modelYear = "2025";
        final var model = "Haval";
        final var manufacturer = "BYD";

        when(searchFilterMapper.toSearchFilter(initialValue,finalValue,model,modelYear,manufacturer)).thenReturn(searchFilter);
        when(searchCarUseCase.execute(pageable,searchFilter)).thenReturn(new PageImpl<>(List.of(carModel)));
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(carDtoResponse);

        final var result = carController.searchAllCars(pageable,initialValue,finalValue,modelYear,model,manufacturer);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data().getContent()).hasSize(1);
        assertThat(result.getBody().data().getContent().getFirst().vin()).isEqualTo(carDtoResponse.vin());
    }

    @Test
    void deleteCarShouldReturnSuccessResponse() throws CarNotFoundException {
        final var vin = "123ABC";

        doNothing().when(deleteCarUseCase).execute(vin);

        final var result = carController.deleteCar(vin);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data()).isEqualTo("Car with VIN: "+ vin +" was deleted successfully");
    }

    @Test
    void deleteCarShouldThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123ABC";
        doThrow(new CarNotFoundException("Car not found"))
                .when(deleteCarUseCase).execute(vin);

        assertThrows(CarNotFoundException.class,() ->carController.deleteCar(vin));
    }

    @Test
    void updateCarShouldReturnUpdatedResponse() throws CarNotFoundException {
        final var vin = "123ABC";
        final var request = Instancio.create(CarDtoUpdateRequest.class);
        final var carModel = Instancio.create(CarModel.class);
        final var response = Instancio.create(CarDtoResponse.class);

        when(updateCarUseCase.execute(vin,request.color(),request.value())).thenReturn(carModel);
        when(carMapper.toCarDtoResponse(carModel)).thenReturn(response);

        final var result = carController.updateCar(vin, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data().vin()).isEqualTo(response.vin());
        assertThat(result.getBody().data().color()).isEqualTo(response.color());
        assertThat(result.getBody().data().value()).isEqualTo(response.value());
    }

    @Test
    void updateCarShouldThrowCarNotFoundException() throws CarNotFoundException {
        final var vin = "123ABC";
        final var request = Instancio.create(CarDtoUpdateRequest.class);

        doThrow(new CarNotFoundException("Car not found"))
                .when(updateCarUseCase).execute(vin,request.color(),request.value());

        assertThrows(CarNotFoundException.class,() ->carController.updateCar(vin, request));
    }
}