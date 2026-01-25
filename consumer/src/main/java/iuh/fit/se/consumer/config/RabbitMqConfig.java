package iuh.fit.se.consumer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "order_queue";
    public static final String DEAD_LETTER_QUEUE_NAME = "order_queue.dlq";

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build();
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", "") // Default exchange
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_NAME)
                .build();
    }
}
