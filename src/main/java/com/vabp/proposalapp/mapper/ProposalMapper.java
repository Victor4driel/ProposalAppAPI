package com.vabp.proposalapp.mapper;

import com.vabp.proposalapp.dto.ProposalRequestDto;
import com.vabp.proposalapp.dto.ProposalResponseDto;
import com.vabp.proposalapp.entity.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.NumberFormat;
import java.util.List;

@Mapper
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    @Mapping(target = "usuario.nome", source = "nome")
    @Mapping(target = "usuario.sobrenome", source = "sobrenome")
    @Mapping(target = "usuario.telefone", source = "telefone")
    @Mapping(target = "usuario.cpf", source = "cpf")
    @Mapping(target = "usuario.renda", source = "renda")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aprovada", ignore = true)
    @Mapping(target = "integrada", constant = "true")
    @Mapping(target = "observacao", ignore = true)
    Proposal convertDtoToProposal(ProposalRequestDto proposalRequestDto);

    @Mapping(target = "nome", source = "usuario.nome")
    @Mapping(target = "sobrenome", source = "usuario.sobrenome")
    @Mapping(target = "cpf", source = "usuario.cpf")
    @Mapping(target = "telefone", source = "usuario.telefone")
    @Mapping(target = "renda", source = "usuario.renda")
    @Mapping(target = "valorSolicitadoFmt", expression = "java(setRequestedValueFmt(proposal))")
    ProposalResponseDto convertEntityToDto(Proposal proposal);

    List<ProposalResponseDto> convertListToDtoList(List<Proposal> proposals);

    default String setRequestedValueFmt(Proposal proposal) {
        return NumberFormat.getCurrencyInstance().format(proposal.getValorSolicitado());
    }
}
