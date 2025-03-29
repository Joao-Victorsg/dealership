package br.com.dealership.car.api.adapter.out.database.repository.specifications;

import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarSpecificationsFactory {

    public static Specification<CarEntity> betweenValues(final BigDecimal initialValue, final BigDecimal finalValue){
        return (root, query, builder) ->
                finalValue == null ? builder.conjunction() : builder.between(root.get("value"),initialValue,finalValue);
    }

    public static Specification<CarEntity> equalModelYear(final String year){
        return (root, query, builder) ->
                year == null ? builder.conjunction() : builder.equal(root.get("modelYear"),year);
    }

    public static Specification<CarEntity> equalModel(final String model){
        return (root, query, builder) ->
                model == null ? builder.conjunction() : builder.equal(builder.lower(root.get("model")),model.toLowerCase());
    }

    public static Specification<CarEntity> equalManufacturer(final String manufacturer){
        return (root, query, builder) ->
                manufacturer == null ? builder.conjunction() : builder.equal(builder.lower(root.get("manufacturer")),manufacturer.toLowerCase());
    }
}