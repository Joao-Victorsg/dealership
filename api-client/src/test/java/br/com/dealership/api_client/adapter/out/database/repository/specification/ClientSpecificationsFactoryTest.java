package br.com.dealership.api_client.adapter.out.database.repository.specification;

import br.com.dealership.api_client.adapter.out.database.entity.ClientEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ClientSpecificationsFactoryTest {

    @Mock
    private Root<ClientEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<Object> cityPath;

    @Mock
    private Path<Object> addressPath;

    @Mock
    private Path<Object> statePath;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnSpecificationForCityWhenCityIsNotNull() {
        final var city = "SomeCity";
        final var expectedPredicate = mock(Predicate.class);

        when(root.get("address")).thenReturn(addressPath);
        when(root.get("address").get("city")).thenReturn(cityPath);
        when(criteriaBuilder.equal(cityPath, city)).thenReturn(expectedPredicate);

        final var specification = ClientSpecificationsFactory.hasCity(city);

        final var predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }

    @Test
    void shouldReturnNullForCityWhenCityIsNull() {
        final var specification = ClientSpecificationsFactory.hasCity(null);
        final Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
        verifyNoInteractions(root, query, criteriaBuilder);
    }

    @Test
    void shouldReturnSpecificationForStateWhenStateIsNotNull() {
        final var stateAbbreviation = "CA";
        final var expectedPredicate = mock(Predicate.class);

        when(root.get("address")).thenReturn(statePath);
        when(root.get("address").get("stateAbbreviation")).thenReturn(statePath);
        when(criteriaBuilder.equal(statePath, stateAbbreviation)).thenReturn(expectedPredicate);

        final Specification<ClientEntity> specification = ClientSpecificationsFactory.hasState(stateAbbreviation);
        final Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        assertEquals(expectedPredicate, predicate);
    }

    @Test
    void shouldReturnNullForStateWhenStateIsNull() {
        final var specification = ClientSpecificationsFactory.hasState(null);
        final Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
        verifyNoInteractions(root, query, criteriaBuilder);
    }
}