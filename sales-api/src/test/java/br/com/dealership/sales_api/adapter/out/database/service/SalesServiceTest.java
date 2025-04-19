package br.com.dealership.sales_api.adapter.out.database.service;

import br.com.dealership.sales_api.adapter.mapper.SalesMapper;
import br.com.dealership.sales_api.adapter.out.database.entity.SalesEntity;
import br.com.dealership.sales_api.adapter.out.database.repository.SalesRepository;
import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesService salesService;

    @Test
    void shouldCreateSaleWithSuccess() {
        final var salesModel = Instancio.create(SalesModel.class);
        final var salesEntity = Instancio.create(SalesEntity.class);

        when(salesMapper.toEntity(salesModel)).thenReturn(salesEntity);
        when(salesRepository.save(salesEntity)).thenReturn(salesEntity);
        when(salesMapper.toModel(salesEntity)).thenReturn(salesModel);

        final var result = assertDoesNotThrow(() -> salesService.create(salesModel));

        assertNotNull(result);
        assertEquals(salesModel, result);
    }

    @Test
    void shouldThrowCarAlreadySoldExceptionWhenTryingToCreateSaleWithCarAlreadySold(){
        final var salesModel = Instancio.create(SalesModel.class);

        when(salesRepository.existsByCarVin(salesModel.vin())).thenReturn(true);

        assertThrows(CarAlreadySoldException.class, () -> salesService.create(salesModel));
    }

    @Test
    void shouldCancelSaleWithSuccess() {
        final var salesId = UUID.randomUUID();

        when(salesRepository.findById(salesId)).thenReturn(Optional.of(Instancio.create(SalesEntity.class)));
        doNothing().when(salesRepository).deleteById(salesId);

        assertDoesNotThrow(() -> salesService.cancel(salesId));
    }

    @Test
    void shouldThrowSaleNotFoundExceptionWhenCancelingNonexistentSale() {
        final var salesId = UUID.randomUUID();

        when(salesRepository.findById(salesId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () -> salesService.cancel(salesId));
    }

    @Test
    void shouldSearchAllSalesWithSuccess() {
        final var pageable = Pageable.ofSize(10);
        final var initialDate = LocalDate.now().minusDays(10);
        final var finalDate = LocalDate.now();
        final var cpf = "12345678900";
        final var salesEntity = Instancio.create(SalesEntity.class);
        final var salesModel = Instancio.create(SalesModel.class);
        final var salesEntities = new PageImpl<>(List.of(salesEntity));

        when(salesRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(salesEntities);
        when(salesMapper.toModel(salesEntity)).thenReturn(salesModel);

        final var result = assertDoesNotThrow(() -> salesService.searchAll(pageable, initialDate, finalDate, cpf));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(salesModel, result.getContent().get(0));
    }

    @Test
    void shouldSearchSaleByIdWithSuccess() {
        final var salesId = UUID.randomUUID();
        final var salesEntity = Instancio.create(SalesEntity.class);
        final var salesModel = Instancio.create(SalesModel.class);

        when(salesRepository.findById(salesId)).thenReturn(Optional.of(salesEntity));
        when(salesMapper.toModel(salesEntity)).thenReturn(salesModel);

        final var result = assertDoesNotThrow(() -> salesService.searchById(salesId));

        assertNotNull(result);
        assertEquals(salesModel, result);
    }

    @Test
    void shouldThrowSaleNotFoundExceptionWhenSearchingById() {
        final var salesId = UUID.randomUUID();

        when(salesRepository.findById(salesId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () -> salesService.searchById(salesId));
    }
}