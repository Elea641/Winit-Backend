package com.cda.winit.tournament.domain.dto;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import com.cda.winit.team.domain.dto.TeamTournamentDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TournamentDetailsDto {
    private Long id;
    private String name;
    private Date date;
    private String place;
    private Integer currentNumberOfParticipants;
    private Integer maxNumberOfTeams;
    private String imageUrl;
    private String sport;
    private Date inscriptionLimitDate;
    private List<TeamTournamentDto> teams;
    private Boolean isGenerated;
    private Boolean isCanceled;
    private Boolean isOwner;
    private Long ownerId;
    private List<MatchDto> matches;
}