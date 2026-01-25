package iuh.fit.se.consumer.service;

import com.rabbitmq.client.Channel;
import iuh.fit.se.consumer.config.RabbitMqConfig;
import iuh.fit.se.consumer.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class OrderConsumer {

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME, ackMode = "MANUAL")
    public void consumeOrder(Order order, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            log.info("Processing: {}", order);

            // Validate orderId
            if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                throw new IllegalArgumentException("Missing orderId");
            }

            // Simulate processing time (3 seconds)
            Thread.sleep(3000);

            log.info("Process success");
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Send to DLQ: {}", e.getMessage());
            try {
                // Send to DLQ (no requeue)
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ioException) {
                log.error("Failed to nack message", ioException);
            }
        }
    }
}
