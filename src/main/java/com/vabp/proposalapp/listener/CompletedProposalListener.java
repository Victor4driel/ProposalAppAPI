package com.vabp.proposalapp.listener;

import com.vabp.proposalapp.entity.Proposal;
import com.vabp.proposalapp.mapper.ProposalMapper;
import com.vabp.proposalapp.repository.ProposalRepository;
import com.vabp.proposalapp.service.WebSocketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {
    private final ProposalRepository proposalRepository;

    private final WebSocketService webSocketService;

    public CompletedProposalListener(ProposalRepository proposalRepository, WebSocketService webSocketService) {
        this.proposalRepository = proposalRepository;
        this.webSocketService = webSocketService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.completed.proposal}")
    public void completedProposal(Proposal proposal) {
        proposalRepository.save(proposal);
        webSocketService.notify(ProposalMapper.INSTANCE.convertEntityToDto(proposal));
    }
}
