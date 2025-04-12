package br.com.dealership.sales_api.core.usecase.port;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.exceptions.SaleNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface SalesServicePort {

    SalesModel create(SalesModel salesModel);

    void cancel(UUID salesId) throws SaleNotFoundException;

    Page<SalesModel> searchAll(final Pageable pageable, final LocalDate initialDate, final LocalDate finalDate, final String cpf);

    SalesModel searchById(UUID salesId) throws SaleNotFoundException;
}