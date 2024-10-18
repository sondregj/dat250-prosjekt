package no.hvl.dat250.polls.messagingrabbitmq;

import no.hvl.dat250.polls.models.Vote;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class VoteConsumer {


    public VoteConsumer() {
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveVote(Vote vote) {
        // TODO: Send vote to database
    }
}

