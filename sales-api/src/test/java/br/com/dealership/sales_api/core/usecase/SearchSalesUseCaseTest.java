package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchSalesUseCaseTest {

    @Mock
    private SalesServicePort salesServicePort;

    @InjectMocks
    private SearchSalesUseCase searchSalesUseCase;

    @Test
    void shouldSearchSaleByIdWithSuccess() throws SaleNotFoundException {
        final var salesId = UUID.randomUUID();
        final var salesModel = Instancio.create(SalesModel.class);

        when(salesServicePort.searchById(salesId)).thenReturn(salesModel);

        final var result = assertDoesNotThrow(() -> searchSalesUseCase.execute(salesId));

        assertNotNull(result);
    }

    @Test
    void shouldThrowSaleNotFoundExceptionWhenSearchingById() throws SaleNotFoundException {
        final var salesId = UUID.randomUUID();

        when(salesServicePort.searchById(salesId)).thenThrow(SaleNotFoundException.class);

        assertThrows(SaleNotFoundException.class, () -> searchSalesUseCase.execute(salesId));
    }

    @Test
    void shouldSearchAllSalesWithSuccess() {
        final var pageable = Pageable.ofSize(10);
        final var initialDate = LocalDate.now().minusDays(10);
        final var finalDate = LocalDate.now();
        final var cpf = "12345678900";
        final var salesModel = Instancio.create(SalesModel.class);
        final var salesPage = new PageImpl<>(List.of(salesModel));

        when(salesServicePort.searchAll(pageable, initialDate, finalDate, cpf)).thenReturn(salesPage);

        final var result = assertDoesNotThrow(() -> searchSalesUseCase.execute(pageable, initialDate, finalDate, cpf));

        assertNotNull(result);
    }
}