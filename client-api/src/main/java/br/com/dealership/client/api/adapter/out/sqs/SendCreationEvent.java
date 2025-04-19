package br.com.dealership.client.api.adapter.out.sqs;

import br.com.dealership.client.api.core.usecase.port.SendCreationEventPort;
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
    public void sendCreationEvent(String cpf) {
        final var message = createMessage(cpf);

        sqsTemplate.sendAsync(message);
    }

    private Message<String> createMessage(String cpf){
        return MessageBuilder.withPayload(cpf)
                .setHeader("eventType", "ClientCreated")
                .build();
    }
}