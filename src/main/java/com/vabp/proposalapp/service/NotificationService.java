package com.vabp.proposalapp.service;

import com.vabp.proposalapp.entity.Proposal;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private RabbitTemplate rabbitTemplate;

    public void notify(Proposal proposal, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "", proposal);
    }
}
