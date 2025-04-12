package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CancelSalesUseCase {

    private final SalesServicePort salesServicePort;

    public void execute(UUID salesId) throws SaleNotFoundException {
        salesServicePort.cancel(salesId);
    }
}