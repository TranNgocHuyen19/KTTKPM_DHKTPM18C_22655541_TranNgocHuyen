package iuh.fit.se.producer.service;

import iuh.fit.se.producer.config.RabbitMqConfig;
import iuh.fit.se.producer.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrder(String message, String orderId) {
        Order order = new Order(message, orderId, LocalDateTime.now().toString());
        
        rabbitTemplate.convertAndSend(RabbitMqConfig.QUEUE_NAME, order);
        
        log.info("Sent: {}", order);
    }
}
