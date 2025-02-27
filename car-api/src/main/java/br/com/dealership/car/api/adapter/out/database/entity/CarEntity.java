package br.com.dealership.car.api.adapter.out.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CARMODEL")
public class CarEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String modelYear;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false,unique = true)
    private String vin;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

}
