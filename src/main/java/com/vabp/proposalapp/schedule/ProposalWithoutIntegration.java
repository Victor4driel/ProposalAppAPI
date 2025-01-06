package com.vabp.proposalapp.schedule;

import com.vabp.proposalapp.entity.Proposal;
import com.vabp.proposalapp.repository.ProposalRepository;
import com.vabp.proposalapp.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class ProposalWithoutIntegration {

    private final ProposalRepository proposalRepository;

    private final NotificationService notificationService;

    private final String exchange;

    private final Logger logger = LoggerFactory.getLogger(ProposalWithoutIntegration.class);

    public ProposalWithoutIntegration(ProposalRepository proposalRepository,
                           NotificationService notificationService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void searchProposalWithoutIntegration() {
        proposalRepository.findAllByIntegradaIsFalse().forEach(proposal -> {
            try {
                notificationService.notify(proposal, exchange);
                updateProposal(proposal);
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
            }
        });
    }

    private void updateProposal(Proposal proposal) {
        proposal.setIntegrada(true);
        proposalRepository.save(proposal);
    }
}
