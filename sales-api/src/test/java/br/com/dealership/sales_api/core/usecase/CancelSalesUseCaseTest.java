package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class CancelSalesUseCaseTest {

    @Mock
    private SalesServicePort salesServicePort;

    @InjectMocks
    private CancelSalesUseCase cancelSalesUseCase;

    @Test
    void shouldCancelSaleWithSuccess() throws SaleNotFoundException {
        final var salesId = UUID.randomUUID();
        doNothing().when(salesServicePort).cancel(salesId);

        assertDoesNotThrow(() -> cancelSalesUseCase.execute(salesId));
    }

    @Test
    void shouldThrowSaleNotFoundException() throws SaleNotFoundException {
        final var salesId = UUID.randomUUID();
        doThrow(SaleNotFoundException.class).when(salesServicePort).cancel(salesId);

        assertThrows(SaleNotFoundException.class, () -> cancelSalesUseCase.execute(salesId));
    }
}