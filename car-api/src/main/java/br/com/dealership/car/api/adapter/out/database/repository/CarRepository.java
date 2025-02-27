package br.com.dealership.car.api.adapter.out.database.repository;

import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID>, JpaSpecificationExecutor<CarEntity> {

    Optional<CarEntity> findByVin(String vehicleIdentificationNumber);

    void deleteByVin(String vehicleIdentificationNumber);
}
