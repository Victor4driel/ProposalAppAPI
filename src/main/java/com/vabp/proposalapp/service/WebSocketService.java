package com.vabp.proposalapp.service;

import com.vabp.proposalapp.dto.ProposalResponseDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notify(ProposalResponseDto responseDto) {
        messagingTemplate.convertAndSend("/proposals", responseDto);
    }
}
