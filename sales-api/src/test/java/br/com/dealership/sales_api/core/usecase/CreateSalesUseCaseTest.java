package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSalesUseCaseTest {

    @Mock
    private SalesServicePort salesServicePort;

    @InjectMocks
    private CreateSalesUseCase createSalesUseCase;

    @Test
    void shouldCreateSaleWithSuccess() throws CarAlreadySoldException {
        final var salesModel = Instancio.create(SalesModel.class);
        when(salesServicePort.create(salesModel)).thenReturn(salesModel);

        final var result = assertDoesNotThrow(() -> createSalesUseCase.execute(salesModel));

        assertNotNull(result);
    }
}