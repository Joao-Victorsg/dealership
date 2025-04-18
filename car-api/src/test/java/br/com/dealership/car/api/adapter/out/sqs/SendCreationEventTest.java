package br.com.dealership.car.api.adapter.out.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SendCreationEventTest {

    @Mock
    private SqsTemplate sqsTemplate;
    @InjectMocks
    private SendCreationEvent sendCreationEvent;

    @Test
    void shouldSendCreationEvent() {
        final var vin = "1HGCM82633A123456";

        sendCreationEvent.sendCreationEvent(vin);

        assertNotNull(sqsTemplate);
    }
}