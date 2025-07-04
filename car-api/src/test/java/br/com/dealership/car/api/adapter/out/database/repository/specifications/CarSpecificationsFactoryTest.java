package br.com.dealership.car.api.adapter.out.database.repository.specifications;

import br.com.dealership.car.api.adapter.out.database.entity.CarEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarSpecificationsFactoryTest {

    @Mock
    private Root<CarEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Test
    void shouldReturnBetweenValues(){
        final var initialValue = BigDecimal.ZERO;
        final var finalValue = BigDecimal.TEN;
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.between(root.get("value"),initialValue,finalValue)).thenReturn(expectedPredicate);

        final var specification = CarSpecificationsFactory.betweenValues(initialValue,finalValue);

        final var predicate = specification.toPredicate(root,query,criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate,predicate);
    }

    @Test
    void shouldReturnEqualModelYear(){
        final var modelYear = "2018";
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("modelYear"),modelYear)).thenReturn(expectedPredicate);

        final var specification = CarSpecificationsFactory.equalModelYear(modelYear);

        final var predicate = specification.toPredicate(root,query,criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate,predicate);
    }

    @Test
    void shouldReturnEqualModel(){
        final var model = "astra";
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("model"),model)).thenReturn(expectedPredicate);

        final var specification = CarSpecificationsFactory.equalModel(model);

        final var predicate = specification.toPredicate(root,query,criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate,predicate);
    }

    @Test
    void shouldReturnEqualManufacturer(){
        final var manufacturer = "gm";
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("manufacturer"),manufacturer)).thenReturn(expectedPredicate);

        final var specification = CarSpecificationsFactory.equalManufacturer(manufacturer);

        final var predicate = specification.toPredicate(root,query,criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate,predicate);
    }

    @Test
    void shouldReturnEqualColor(){
        final var color = "black";
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("color"),color)).thenReturn(expectedPredicate);

        final var specification = CarSpecificationsFactory.equalColor(color);

        final var predicate = specification.toPredicate(root,query,criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate,predicate);
    }
}