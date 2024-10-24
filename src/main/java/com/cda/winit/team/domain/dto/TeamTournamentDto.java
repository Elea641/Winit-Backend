package com.cda.winit.team.domain.dto;

import lombok.Data;

@Data
public class TeamTournamentDto {
    private Long id;
    private String name;
    private String sport;
    private String leaderName;
    private Long ownerId;
    private Long currentUser;
}