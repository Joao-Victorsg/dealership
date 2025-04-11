package br.com.dealership.sales_api.adapter.out.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_Sales")
public class SalesEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "client_cpf",nullable = false)
    private ClientEntity cpf;

    @OneToOne
    @JoinColumn(name = "car_vin", unique = true, nullable = false)
    private CarEntity vin;

    @Column(nullable = false)
    private LocalDateTime registrationDate;
}