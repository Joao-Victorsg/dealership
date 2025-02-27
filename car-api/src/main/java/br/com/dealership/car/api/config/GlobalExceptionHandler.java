package br.com.dealership.car.api.config;

import br.com.dealership.car.api.adapter.in.dto.response.Response;
import br.com.dealership.car.api.adapter.in.dto.response.ResponseError;
import br.com.dealership.car.api.core.exceptions.CarAlreadyExistsException;
import br.com.dealership.car.api.core.exceptions.CarNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class GlobalExceptionHandler<T> extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CarAlreadyExistsException.class})
    protected ResponseEntity<Response<ResponseError>> handleCarAlreadyExistsException(CarAlreadyExistsException exception){
        var response = buildResponseException(exception);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = {CarNotFoundException.class})
    protected ResponseEntity<Response<ResponseError>> handleCarNotFoundException(CarNotFoundException exception){
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