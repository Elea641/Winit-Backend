package com.cda.winit.match.domain.dto;

import lombok.Data;

@Data
public class MatchUpdateDto {
    private Long id;
    private Long tournamentId;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
    private NextTeamInfo nextTeamInfo;
}
