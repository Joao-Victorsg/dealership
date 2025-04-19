package br.com.dealership.sales_api.adapter.out.database.repository.specification;

import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesSpecificationsFactoryTest {

    @Mock
    private Root<SalesEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private Path<Object> clientPath;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Test
    void shouldReturnBetweenDatesPredicate() {
        final var initialDate = LocalDate.now().minusDays(10);
        final var finalDate = LocalDate.now();
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.between(root.get("registrationDate"), initialDate, finalDate)).thenReturn(expectedPredicate);

        final var specification = SalesSpecificationsFactory.betweenDates(initialDate, finalDate);

        final var predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }

    @Test
    void shouldReturnConjunctionWhenDatesAreNull() {
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.conjunction()).thenReturn(expectedPredicate);

        final var specification = SalesSpecificationsFactory.betweenDates(null, null);

        final var predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }

    @Test
    void shouldReturnHasCpfPredicate() {
        final var cpf = "12345678900";
        final var expectedPredicate = mock(Predicate.class);

        when(root.get("client")).thenReturn(clientPath);
        when(criteriaBuilder.equal(root.get("client").get("cpf"), cpf)).thenReturn(expectedPredicate);

        final var specification = SalesSpecificationsFactory.hasCpf(cpf);

        final var predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }

    @Test
    void shouldReturnConjunctionWhenCpfIsNull() {
        final var expectedPredicate = mock(Predicate.class);

        when(criteriaBuilder.conjunction()).thenReturn(expectedPredicate);

        final var specification = SalesSpecificationsFactory.hasCpf(null);

        final var predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }
}