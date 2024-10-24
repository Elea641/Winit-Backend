package com.cda.winit.match.domain.dto;

import lombok.Data;

@Data
public class TopWinnerTeamDto {
    private Long id;
    private String name;
    private String sport;
    private Long ownerId;
    private String tournamentName;
}
