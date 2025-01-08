package com.vabp.proposalapp.listener;

import com.vabp.proposalapp.entity.Proposal;
import com.vabp.proposalapp.repository.ProposalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompletedProposalListener {
    private final ProposalRepository proposalRepository;

    public CompletedProposalListener(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.completed.proposal}")
    public void completedProposal(Proposal proposal) {
        proposalRepository.save(proposal);
    }

}
