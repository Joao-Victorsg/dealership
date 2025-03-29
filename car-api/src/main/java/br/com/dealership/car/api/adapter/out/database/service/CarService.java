package br.com.dealership.car.api.adapter.out.database.service;

import br.com.dealership.car.api.adapter.mapper.CarMapper;
import br.com.dealership.car.api.adapter.out.database.repository.CarRepository;
import br.com.dealership.car.api.core.domain.CarModel;
import br.com.dealership.car.api.core.domain.SearchFilter;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import br.com.dealership.car.api.core.usecase.port.CarServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static br.com.dealership.car.api.adapter.out.database.repository.specifications.CarSpecificationsFactory.betweenValues;
import static br.com.dealership.car.api.adapter.out.database.repository.specifications.CarSpecificationsFactory.equalManufacturer;
import static br.com.dealership.car.api.adapter.out.database.repository.specifications.CarSpecificationsFactory.equalModel;
import static br.com.dealership.car.api.adapter.out.database.repository.specifications.CarSpecificationsFactory.equalModelYear;

@RequiredArgsConstructor
@Service
public class CarService implements CarServicePort {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarModel findByVin(final String vin) throws CarNotFoundException {
        final var carEntity = carRepository.findByVin(vin)
                .orElseThrow(() -> new CarNotFoundException("There isn't a car with this VIN"));

        return carMapper.toCarModel(carEntity);
    }

    @Override
    public Page<CarModel> searchAll(final SearchFilter searchFilter, final Pageable pageable) {
        final var specification = Specification.where(betweenValues(searchFilter.initialValue(),searchFilter.finalValue()))
                .and(equalModelYear(searchFilter.modelYear()))
                .and(equalModel(searchFilter.model()))
                .and(equalManufacturer(searchFilter.manufacturer()));

        final var carsEntities = carRepository.findAll(specification,pageable);

        final var carModels = carsEntities.stream()
                .map(carMapper::toCarModel)
                .toList();

        return new PageImpl<>(carModels,carsEntities.getPageable(),carModels.size());
    }

    @Override
    public CarModel create(final CarModel carModel) throws CarAlreadyExistsException {
        if(carRepository.findByVin(carModel.vin()).isPresent())
            throw new CarAlreadyExistsException("A car with this VIN already exists");

        final var entity = carMapper.toEntity(carModel);

        final var savedEntity = carRepository.save(entity);

        return carMapper.toCarModel(savedEntity);
    }

    @Transactional
    @Override
    public void delete(final String vin) throws CarNotFoundException {
        carRepository.findByVin(vin)
                .orElseThrow(() -> new CarNotFoundException("A car with this VIN was not found"));

        carRepository.deleteByVin(vin);
    }

    @Transactional
    @Override
    public CarModel update(final String vin, final String color, final BigDecimal value) throws CarNotFoundException{
        final var entity = carRepository.findByVin(vin)
                .orElseThrow(() -> new CarNotFoundException("A car with this VIN was not found"));

        final var updatedCarToSave = entity.toBuilder()
                .color(color)
                .value(value)
                .build();

        final var updatedCar = carRepository.save(updatedCarToSave);

        return carMapper.toCarModel(updatedCar);
    }
}