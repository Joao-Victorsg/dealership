package br.com.dealership.sales_api.adapter.out.sns;

import br.com.dealership.sales_api.core.domain.SalesModel;
import br.com.dealership.sales_api.core.usecase.port.SendSalesEventPort;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSalesEventService implements SendSalesEventPort {

    private final SnsTemplate snsTemplate;

    @Value("${sales.sns.topic.arn}")
    private String topicArn;

    @Override
    public void sendSalesEvent(SalesModel saleCompleted) {
        snsTemplate.convertAndSend(topicArn, saleCompleted);
    }
}