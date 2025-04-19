package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateSalesUseCase {

    private final SalesServicePort salesServicePort;

    public SalesModel execute(SalesModel salesModel) throws CarAlreadySoldException {
        return salesServicePort.create(salesModel);
    }
}