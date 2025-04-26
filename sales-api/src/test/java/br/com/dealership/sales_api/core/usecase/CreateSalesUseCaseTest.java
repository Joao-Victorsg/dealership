package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import br.com.dealership.sales_api.core.usecase.port.SendSalesEventPort;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSalesUseCaseTest {

    @Mock
    private SalesServicePort salesServicePort;

    @Mock
    private SendSalesEventPort sendSalesEventPort;

    @InjectMocks
    private CreateSalesUseCase createSalesUseCase;

    @Test
    void shouldCreateSaleWithSuccess() throws CarAlreadySoldException {
        // Arrange
        final var salesModel = Instancio.create(SalesModel.class);
        when(salesServicePort.create(salesModel)).thenReturn(salesModel);

        // Act
        final var result = assertDoesNotThrow(() -> createSalesUseCase.execute(salesModel));

        // Assert
        assertNotNull(result);
        verify(salesServicePort).create(salesModel);
        verify(sendSalesEventPort).sendSalesEvent(salesModel);
    }

    @Test
    void shouldThrowCarAlreadySoldExceptionWhenCarIsSold() throws CarAlreadySoldException {
        final var salesModel = Instancio.create(SalesModel.class);
        when(salesServicePort.create(salesModel))
                .thenThrow(new CarAlreadySoldException("Car is already sold"));

        final var exception = assertThrows(
                CarAlreadySoldException.class,
                () -> createSalesUseCase.execute(salesModel)
        );

        Assertions.assertEquals("Car is already sold", exception.getMessage());
        verify(salesServicePort).create(salesModel);
        verify(sendSalesEventPort, never()).sendSalesEvent(any());
    }

}
