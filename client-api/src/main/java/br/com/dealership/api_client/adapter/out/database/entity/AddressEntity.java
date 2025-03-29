package br.com.dealership.api_client.adapter.out.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tb_address")
public class AddressEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @UuidGenerator
    private String id;

    private String city;

    @NotBlank
    private String postCode;

    private String stateAbbreviation;

    private String streetName;

    private String streetNumber;

    private Boolean isAddressSearched;
}
