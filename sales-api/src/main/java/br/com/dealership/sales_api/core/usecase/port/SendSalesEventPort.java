package br.com.dealership.sales_api.core.usecase.port;

import br.com.dealership.sales_api.core.domain.SalesModel;

public interface SendSalesEventPort {

    void sendSalesEvent(SalesModel saleCompleted);
}
