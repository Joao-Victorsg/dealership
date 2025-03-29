package br.com.dealership.car.api.adapter.out.database.service;

import br.com.dealership.car.api.adapter.mapper.CarMapper;
import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import br.com.dealership.car.api.adapter.out.database.repository.CarRepository;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarService carService;

    @Test
    void shouldFindByVinWithSuccess(){
        final var vin = "123";
        final var carEntity = Instancio.create(CarEntity.class);
        final var carModel = Instancio.create(CarModel.class);

        when(carRepository.findByVin(vin)).thenReturn(Optional.of(carEntity));
        when(carMapper.toCarModel(carEntity)).thenReturn(carModel);

        final var resultado = assertDoesNotThrow(() -> carService.findByVin(vin));

        assertNotNull(resultado);
        assertEquals(carModel,resultado);
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenThereIsntCarWithVin(){
        final var vin = "123";

        when(carRepository.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> carService.findByVin(vin));
    }

    @Test
    void shouldSearchAllCarsWithSuccess() {
        final var searchFilter = Instancio.create(SearchFilter.class);
        final var pageable = Pageable.ofSize(10);
        final var carEntity = Instancio.create(CarEntity.class);
        final var carModel = Instancio.create(CarModel.class);
        final var carEntities = new PageImpl<>(List.of(carEntity));

        when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(carEntities);
        when(carMapper.toCarModel(carEntity)).thenReturn(carModel);

        final var result = assertDoesNotThrow(() -> carService.searchAll(searchFilter, pageable));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(carModel, result.getContent().getFirst());
    }

    @Test
    void shouldCreateCarWithSuccess(){
        final var carModel = Instancio.create(CarModel.class);
        final var carEntity = Instancio.create(CarEntity.class);

        when(carRepository.findByVin(carModel.vin())).thenReturn(Optional.empty());
        when(carMapper.toEntity(carModel)).thenReturn(carEntity);
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carMapper.toCarModel(carEntity)).thenReturn(carModel);

        final var resultado = assertDoesNotThrow(() -> carService.create(carModel));

        assertNotNull(resultado);
        assertEquals(carModel,resultado);
    }

    @Test
    void shouldThrowCarAlreadyExistsExceptionIfCarAlreadyExists(){
        final var carModel = Instancio.create(CarModel.class);
        final var carEntity = Instancio.create(CarEntity.class);

        when(carRepository.findByVin(carModel.vin())).thenReturn(Optional.of(carEntity));

        assertThrows(CarAlreadyExistsException.class,()-> carService.create(carModel));
    }

    @Test
    void shouldDeleteCarWithSuccess(){
        final var carVin = "123";
        final var carEntity = Instancio.create(CarEntity.class);

        when(carRepository.findByVin(carVin)).thenReturn(Optional.of(carEntity));
        doNothing().when(carRepository).deleteByVin(carVin);

        assertDoesNotThrow(() -> carService.delete(carVin));
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenDeletingInexistentCar(){
        final var carVin = "123";

        when(carRepository.findByVin(carVin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,() -> carService.delete(carVin));
    }

    @Test
    void shouldUpdateCarWithSuccess(){
        final var vin = "123";
        final var color = "Red";
        final var value = BigDecimal.valueOf(30000);
        final var carEntity = Instancio.create(CarEntity.class);
        final var updatedCarEntity = carEntity.toBuilder().color(color).value(value).build();
        final var updatedCarModel = Instancio.create(CarModel.class);

        when(carRepository.findByVin(vin)).thenReturn(Optional.of(carEntity));
        when(carRepository.save(any(CarEntity.class))).thenReturn(updatedCarEntity);
        when(carMapper.toCarModel(updatedCarEntity)).thenReturn(updatedCarModel);

        final var result = assertDoesNotThrow(() -> carService.update(vin, color, value));

        assertNotNull(result);
        assertEquals(updatedCarModel, result);
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenUpdating(){
        final var vin = "123";
        final var color = "Red";
        final var value = BigDecimal.valueOf(30000);

        when(carRepository.findByVin(vin)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.update(vin, color, value));
    }

}