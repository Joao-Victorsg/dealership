package br.com.dealership.sales_api.core.exceptions;

public class DuplicatedSalesIdException extends RuntimeException {
    public DuplicatedSalesIdException(String message) {
        super(message);
    }
}
