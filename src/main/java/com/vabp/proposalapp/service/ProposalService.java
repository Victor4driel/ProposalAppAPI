package com.vabp.proposalapp.service;

import com.vabp.proposalapp.dto.ProposalRequestDto;
import com.vabp.proposalapp.dto.ProposalResponseDto;
import com.vabp.proposalapp.entity.Proposal;
import com.vabp.proposalapp.mapper.ProposalMapper;
import com.vabp.proposalapp.repository.ProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepository;

    private final NotificationService notificationService;

    private String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationService notificationService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationService;
        this.exchange = exchange;
    }

    public ProposalResponseDto create(ProposalRequestDto requestDto) {
        Proposal proposal = ProposalMapper.INSTANCE.convertDtoToProposal(requestDto);
        proposalRepository.save(proposal);

        notifyRabbitMQ(proposal);

        return ProposalMapper.INSTANCE.convertEntityToDto(proposal);
    }

    private void notifyRabbitMQ(Proposal proposal) {
        try {
            notificationService.notify(proposal, exchange);
        } catch (RuntimeException e) {
            proposal.setIntegrada(false);
            proposalRepository.save(proposal);
        }
    }

    public List<ProposalResponseDto> findAll() {
        List<Proposal> proposals = proposalRepository.findAll();

        return ProposalMapper.INSTANCE.convertListToDtoList(proposals);
    }
}
