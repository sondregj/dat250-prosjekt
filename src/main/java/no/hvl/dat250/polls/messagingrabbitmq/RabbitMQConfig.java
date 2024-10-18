package no.hvl.dat250.polls.messagingrabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "votesQueue";
    public static final String EXCHANGE_NAME = "votesExchange";
    public static final String ROUTING_KEY = "votesRoutingKey";

    @Bean
    public Queue votesQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange votesExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(votesQueue()).to(votesExchange()).with(ROUTING_KEY);
    }
}
