package br.com.dealership.sales_api.adapter.out.sns;

import br.com.dealership.sales_api.core.domain.SalesModel;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendSalesEventServiceTest {

    private static final String TOPIC_ARN = "mock-topic-arn";

    @Mock
    private SnsTemplate snsTemplate;

    @InjectMocks
    private SendSalesEventService sendSalesEventService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sendSalesEventService, "topicArn", TOPIC_ARN);
    }

    @Test
    void shouldSendSalesEventSuccessfully() {
        final var salesModel = Instancio.create(SalesModel.class);
        
        sendSalesEventService.sendSalesEvent(salesModel);
        
        verify(snsTemplate, times(1)).convertAndSend(TOPIC_ARN, salesModel);
    }
}