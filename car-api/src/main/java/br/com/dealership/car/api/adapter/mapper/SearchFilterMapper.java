package br.com.dealership.car.api.adapter.mapper;

import br.com.dealership.car.api.core.domain.SearchFilter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@NoArgsConstructor
@Component
public class SearchFilterMapper {

    public SearchFilter toSearchFilter(BigDecimal initialValue, BigDecimal finalValue, String model, String modelYear,String manufacturer,String color){
        return SearchFilter.builder()
                .initialValue(initialValue)
                .finalValue(finalValue)
                .model(model)
                .modelYear(modelYear)
                .manufacturer(manufacturer)
                .color(color)
                .build();
    }
}