package no.hvl.dat250.polls.messagingrabbitmq;

import no.hvl.dat250.polls.models.Poll;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PollProducer {

    private final RabbitTemplate rabbitTemplate;

    public PollProducer() {
        RabbitMQConfig config = new RabbitMQConfig();
        this.rabbitTemplate = config.rabbitTemplate();
    }

    public void send(Poll poll) {
        System.out.println("Sending message...");

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, poll);
        System.out.println("Sent poll: " + poll.getId());
    }
}
