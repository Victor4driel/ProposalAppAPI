package com.vabp.proposalapp.controller;

import com.vabp.proposalapp.dto.ProposalRequestDto;
import com.vabp.proposalapp.dto.ProposalResponseDto;
import com.vabp.proposalapp.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/proposta")
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDto> create(@RequestBody ProposalRequestDto requestDto) {
        ProposalResponseDto response = proposalService.create(requestDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProposalResponseDto>> getAll() {
        List<ProposalResponseDto> response = proposalService.findAll();

        return ResponseEntity.ok(response);
    }
}
