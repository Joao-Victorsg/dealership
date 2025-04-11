package br.com.dealership.sales_api.adapter.in.controller;


import br.com.dealership.sales_api.adapter.in.dto.request.SalesDtoRequest;
import br.com.dealership.sales_api.adapter.in.dto.response.Response;
import br.com.dealership.sales_api.adapter.in.dto.response.SalesDtoResponse;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.CancelSalesUseCase;
import br.com.dealership.sales_api.core.usecase.CreateSalesUseCase;
import br.com.dealership.sales_api.core.usecase.SearchSalesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;

import static br.com.dealership.sales_api.adapter.in.dto.response.Response.createResponse;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class SalesController {

    private final CreateSalesUseCase createSalesUseCase;
    private final SearchSalesUseCase searchSalesUseCase;
    private final CancelSalesUseCase cancelSalesUseCase;
    private final SalesMapper salesMapper;

    @Operation(summary="Save a sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The sale was created with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "409", description = "There was a conflict when creating the sale"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @PostMapping(path = "/sales")
    public ResponseEntity<Response<SalesDtoResponse>> saveSale(@RequestBody @Valid final SalesDtoRequest request) {

        final var salesModel = salesMapper.toSalesModel(request);

        final var createdSales = createSalesUseCase.execute(salesModel);

        final var salesDtoResponse = salesMapper.toSalesDtoResponse(createdSales);

        final var response = Response.createResponse(salesDtoResponse);

        return ResponseEntity.created(URI.create("/v1/dealership/sales/" + salesDtoResponse.id()))
                .body(response);
    }

    @Operation(summary="Return a page of sales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return a list of sales"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path= "/sales",produces = "application/json")
    public ResponseEntity<Response<PageImpl<SalesDtoResponse>>> searchAllSales(@PageableDefault(sort ="id",
            direction = Sort.Direction.ASC) final Pageable pageable, @RequestParam(required = false) final LocalDate initialDate,
                                                                               @RequestParam(required = false) final LocalDate finalDate,
                                                                               @RequestParam(required = false) final String cpf){

        //TODO: Incluir parametro para poder filtrar pelo cpf do cliente

        final var sales = searchSalesUseCase.execute(pageable,initialDate,finalDate,cpf);

        final var salesDtoResponse = sales.stream()
                .map(salesMapper::toSalesDtoResponse)
                .toList();

        final var response = Response.createResponse(new PageImpl<>(salesDtoResponse,sales.getPageable(),sales.getTotalElements()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="Return one sale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The sale was returned with success"),
            @ApiResponse(responseCode = "400", description = "The server cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404",description = "There wasn't a sale with the ID that was informed."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @GetMapping(path = "/sales/{id}", produces = "application/json")
    public ResponseEntity<Response<SalesDtoResponse>> searchSale(@PathVariable(value = "id") final String id){
        var sale = searchSalesUseCase.execute(id);

        final var response = Response.createResponse(salesMapper.toSalesDtoResponse(sale));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a sale passing the CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The sale was deleted with success"),
            @ApiResponse(responseCode = "404",description = "There wasn't a sale with the Id that was informed in the database."),
            @ApiResponse(responseCode = "408", description = "The request timed out"),
            @ApiResponse(responseCode = "500", description = "There was internal server erros"),
            @ApiResponse(responseCode = "502", description = "Bad Gateway, the server got a invalid response"),
            @ApiResponse(responseCode = "503", description = "The service is unaivalable"),
            @ApiResponse(responseCode = "504", description = "The Gateway timed out")
    })
    @DeleteMapping(path = "/sales/{id}", produces = "application/json")
    public ResponseEntity<Response<String>> deleteSales(@PathVariable(value = "id") final String id){

        cancelSalesUseCase.execute(id);

        final var response = createResponse("The sale with ID: " + id + " was deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
