package com.cda.winit.tournament.domain.dto;

import com.cda.winit.match.domain.dto.MatchesDto;
import lombok.Data;

@Data
public class TournamentUpdateDto {
    private Boolean isGenerated;
    private Boolean isCanceled;
    private MatchesDto matches;
}