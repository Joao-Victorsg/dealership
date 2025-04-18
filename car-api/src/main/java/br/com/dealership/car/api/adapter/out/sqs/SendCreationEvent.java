package br.com.dealership.car.api.adapter.out.sqs;

import br.com.dealership.car.api.core.usecase.port.SendCreationEventPort;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SendCreationEvent implements SendCreationEventPort {

    private final SqsTemplate sqsTemplate;

    @Override
    public void sendCreationEvent(String vin) {
        final var message = createMessage(vin);

        sqsTemplate.sendAsync(message);
    }

    private Message<String> createMessage(String vin){
        return MessageBuilder.withPayload(vin)
                .setHeader("eventType", "CarCreated")
                .build();
    }
}
