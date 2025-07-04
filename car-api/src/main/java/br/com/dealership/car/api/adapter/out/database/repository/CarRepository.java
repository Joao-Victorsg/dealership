package br.com.dealership.car.api.adapter.out.database.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;

@Repository
public interface CarRepository
        extends JpaRepository<CarEntity, String>, JpaSpecificationExecutor<CarEntity> {

    Optional<CarEntity> findByVin(String vehicleIdentificationNumber);

    void deleteByVin(String vehicleIdentificationNumber);

    @Query(value = "SELECT DISTINCT manufacturer FROM TB_CARMODEL ORDER BY manufacturer",
            nativeQuery = true)
    List<String> findDistinctManufacturersNative();

    @Query(value = "SELECT DISTINCT model FROM TB_CARMODEL ORDER BY model", nativeQuery = true)
    List<String> findDistinctModelsNative();

    @Query(value = "SELECT DISTINCT model FROM TB_CARMODEL WHERE manufacturer = :manufacturer ORDER BY model",
            nativeQuery = true)
    List<String> findDistinctModelsByManufacturerNative(@Param("manufacturer") String manufacturer);
}
