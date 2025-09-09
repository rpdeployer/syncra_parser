package ru.syncra.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.syncra.entities.dto.MessageUpdatePayload;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class MessageUpdateProducer {

    private final RabbitTemplate rabbitTemplate;
    public final String messageUpdateQueue;
    private final ObjectMapper objectMapper;

    public MessageUpdateProducer(RabbitTemplate rabbitTemplate,
                             @Value("${spring.queues.message-update-queue}") String messageUpdateQueue,
                             ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageUpdateQueue = messageUpdateQueue;
        this.objectMapper = objectMapper;
    }

    public void sendToQueue(String queue, MessageUpdatePayload message) {
        try {
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(message);

            sendRawJson(queue, json);
            log.info("Сообщение отправлено в очередь {}: {}", queue, json);
        } catch (Exception e) {
            log.error("Ошибка при обработке JSON для очереди {}: {}", queue, e.getMessage(), e);
            throw new RuntimeException("Ошибка при отправке JSON в RabbitMQ", e);
        }
    }

    public void sendRawJson(String queueName, String rawJsonMessage) {
        Message message = MessageBuilder.withBody(rawJsonMessage.getBytes())
                .setContentType("application/json")
                .build();

        rabbitTemplate.send(queueName, message);
        System.out.println("Сообщение отправлено в очередь: " + queueName);
    }

}
