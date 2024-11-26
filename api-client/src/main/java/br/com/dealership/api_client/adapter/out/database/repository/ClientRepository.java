package br.com.dealership.api_client.adapter.out.database.repository;

import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String>, JpaSpecificationExecutor<ClientEntity> {

    Optional<ClientEntity> findByCpf(String cpf);

    void deleteByCpf(String cpf);
}