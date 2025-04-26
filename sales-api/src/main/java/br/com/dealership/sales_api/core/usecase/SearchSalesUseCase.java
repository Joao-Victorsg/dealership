package br.com.dealership.sales_api.core.usecase;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import br.com.dealership.sales_api.core.usecase.port.SalesServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class SearchSalesUseCase {

    private final SalesServicePort salesServicePort;

    public SalesModel execute(final UUID salesId) throws SaleNotFoundException {
        return salesServicePort.searchById(salesId);
    }

    public Page<SalesModel> execute(final Pageable pageable, final LocalDate initialDate, final LocalDate finalDate, final String cpf){
        return salesServicePort.searchAll(pageable,initialDate,finalDate,cpf);
    }
}