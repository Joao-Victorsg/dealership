package br.com.dealership.api_client.config.rest.handlers;


import br.com.dealership.api_client.adapter.in.controller.dto.response.Response;
import br.com.dealership.api_client.adapter.in.controller.dto.response.ResponseError;
import br.com.dealership.api_client.core.exceptions.ClientAlreadyExistsException;
import br.com.dealership.api_client.core.exceptions.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class GlobalExceptionHandler<T> extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ClientAlreadyExistsException.class})
    protected ResponseEntity<Response<ResponseError>> handleClientAlreadyExistsException(ClientAlreadyExistsException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {ClientNotFoundException.class})
    protected ResponseEntity<Response<ResponseError>> handleClientNotFoundException(ClientNotFoundException exception){
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