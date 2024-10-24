package com.cda.winit.match.domain.dto;

import lombok.Data;

@Data
public class MatchDto {
    private Long id;
    private String phase;
    private String team1;
    private String team2;
    private Long winnerTeamId;
    private Long loserTeamId;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
}
