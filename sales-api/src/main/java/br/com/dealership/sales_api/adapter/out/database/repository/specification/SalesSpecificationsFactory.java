package br.com.dealership.sales_api.adapter.out.database.repository.specification;

import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SalesSpecificationsFactory {

    public static Specification<SalesEntity> betweenDates(final LocalDate initialDate, final LocalDate finalDate){
        final var isStartOrFinalDateNull = initialDate == null || finalDate == null;

        return (root, query, builder) ->
                isStartOrFinalDateNull ? builder.conjunction() : builder.between(root.get("registrationDate"),initialDate,finalDate);
    }

    public static Specification<SalesEntity> hasCpf(final String cpf){
        return (root, query, builder) ->
                cpf == null ? builder.conjunction() : builder.equal(root.get("client").get("cpf"),cpf);
    }
}