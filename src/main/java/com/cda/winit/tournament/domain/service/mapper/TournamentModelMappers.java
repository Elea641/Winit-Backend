package com.cda.winit.tournament.domain.service.mapper;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.pagination.shared.interfaces.IPaginableEntityToModelMappers;
import com.cda.winit.team.domain.dto.TeamTournamentDto;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.dto.TournamentCarouselDTO;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TournamentModelMappers implements IPaginableEntityToModelMappers<Tournament, TournamentCarouselDTO> {

    @Override
    public TournamentCarouselDTO ToDto(Tournament entity) {
        TournamentCarouselDTO dto = new TournamentCarouselDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        dto.setPlace(entity.getPlace());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCurrentNumberOfParticipants(entity.getCurrentNumberOfParticipants());
        dto.setMaxNumberOfTeams(entity.getMaxNumberOfTeams());
        dto.setSport(entity.getSport().getName());

        return dto;
    }
}
