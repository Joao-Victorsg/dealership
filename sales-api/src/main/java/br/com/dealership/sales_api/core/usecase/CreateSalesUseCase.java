package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.CarAlreadySoldException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import br.com.dealership.sales_api.core.usecase.port.SendSalesEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateSalesUseCase {

    private final SalesServicePort salesServicePort;
    private final SendSalesEventPort sendSalesEventPort;

    public SalesModel execute(SalesModel salesModel) throws CarAlreadySoldException {

        final var saleCompleted = salesServicePort.create(salesModel);

        sendSalesEventPort.sendSalesEvent(saleCompleted);

        return saleCompleted;
    }
}