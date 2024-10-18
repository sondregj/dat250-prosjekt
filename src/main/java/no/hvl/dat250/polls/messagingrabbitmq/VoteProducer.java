package no.hvl.dat250.polls.messagingrabbitmq;

import no.hvl.dat250.polls.models.Vote;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public VoteProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Vote vote) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, vote);
        System.out.println(" [x] Sent '" + vote + "'");
    }

}
