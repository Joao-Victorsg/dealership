package br.com.dealership.sales_api.adapter.out.database.repository;

import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesRepository extends JpaRepository<SalesEntity, UUID>, JpaSpecificationExecutor<SalesEntity> {

    Optional<SalesEntity> findById(UUID salesId);

    void deleteById(UUID salesId);
}