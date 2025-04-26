package br.com.dealership.sales_api.adapter.in.controller;

import br.com.dealership.sales_api.adapter.in.controller.dto.request.SalesDtoRequest;
import br.com.dealership.sales_api.adapter.in.controller.dto.response.SalesDtoResponse;
import br.com.dealership.sales_api.adapter.mapper.SalesMapper;
import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.CancelSalesUseCase;
import br.com.dealership.sales_api.core.usecase.CreateSalesUseCase;
import br.com.dealership.sales_api.core.usecase.SearchSalesUseCase;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesControllerTest {

    @Mock
    private CreateSalesUseCase createSalesUseCase;

    @Mock
    private SearchSalesUseCase searchSalesUseCase;

    @Mock
    private CancelSalesUseCase cancelSalesUseCase;

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesController salesController;

    @Test
    void createSaleShouldReturnCreatedResponse() throws CarAlreadySoldException {
        final var request = Instancio.create(SalesDtoRequest.class);
        final var salesModel = Instancio.create(SalesModel.class);
        final var response = Instancio.create(SalesDtoResponse.class);

        when(salesMapper.toModel(request)).thenReturn(salesModel);
        when(createSalesUseCase.execute(salesModel)).thenReturn(salesModel);
        when(salesMapper.toDto(salesModel)).thenReturn(response);

        final var result = salesController.createSale(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data()).isEqualTo(response);
    }

    @Test
    void searchAllSalesShouldReturnPageOfSales() {
        final var pageable = Pageable.ofSize(10);
        final var salesModel = Instancio.create(SalesModel.class);
        final var salesDtoResponse = Instancio.create(SalesDtoResponse.class);
        final var initialDate = LocalDate.now().minusDays(10);
        final var finalDate = LocalDate.now();
        final var cpf = "12345678900";

        when(searchSalesUseCase.execute(pageable, initialDate, finalDate, cpf)).thenReturn(new PageImpl<>(List.of(salesModel)));
        when(salesMapper.toDto(salesModel)).thenReturn(salesDtoResponse);

        final var result = salesController.searchAllSales(pageable, initialDate, finalDate, cpf);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data().getContent()).hasSize(1);
        assertThat(result.getBody().data().getContent().get(0)).isEqualTo(salesDtoResponse);
    }

    @Test
    void searchSaleShouldReturnSale() throws SaleNotFoundException {
        final var id = UUID.randomUUID().toString();
        final var salesModel = Instancio.create(SalesModel.class);
        final var salesDtoResponse = Instancio.create(SalesDtoResponse.class);

        when(searchSalesUseCase.execute(UUID.fromString(id))).thenReturn(salesModel);
        when(salesMapper.toDto(salesModel)).thenReturn(salesDtoResponse);

        final var result = salesController.searchSale(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data()).isEqualTo(salesDtoResponse);
    }

    @Test
    void searchSaleShouldThrowSaleNotFoundException() throws SaleNotFoundException {
        final var id = UUID.randomUUID().toString();

        doThrow(new SaleNotFoundException("Sale not found")).when(searchSalesUseCase).execute(UUID.fromString(id));

        assertThrows(SaleNotFoundException.class, () -> salesController.searchSale(id));
    }

    @Test
    void cancelSaleShouldReturnSuccessResponse() throws SaleNotFoundException {
        final var id = UUID.randomUUID().toString();

        doNothing().when(cancelSalesUseCase).execute(UUID.fromString(id));

        final var result = salesController.cancelSale(id);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().data()).isEqualTo("The sale with ID: " + id + " was deleted successfully");
    }

    @Test
    void cancelSaleShouldThrowSaleNotFoundException() throws SaleNotFoundException {
        final var id = UUID.randomUUID().toString();

        doThrow(new SaleNotFoundException("Sale not found")).when(cancelSalesUseCase).execute(UUID.fromString(id));

        assertThrows(SaleNotFoundException.class, () -> salesController.cancelSale(id));
    }
}