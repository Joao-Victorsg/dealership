package br.com.dealership.sales_api.config;

import br.com.dealership.sales_api.adapter.in.controller.dto.response.Response;
import br.com.dealership.sales_api.adapter.in.controller.dto.response.ResponseError;
import br.com.dealership.sales_api.core.exceptions.DuplicatedSalesIdException;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class GlobalExceptionHandler<T> extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DuplicatedSalesIdException.class})
    protected ResponseEntity<Response<ResponseError>> handleDuplicatedSaleExistsException(DuplicatedSalesIdException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {SaleNotFoundException.class})
    protected ResponseEntity<Response<ResponseError>> handleSaleNotFoundException(SaleNotFoundException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    private Response<ResponseError> buildResponseException(Exception ex){
        return Response.createResponseWithError(ResponseError.builder()
                .timestamp(LocalDateTime.now(ZoneId.of("UTC")))
                .details(ex.getLocalizedMessage())
                .build()
        );
    }
}