package com.cda.winit.match.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchesDto {
    private List<MatchDto> otherMatches;
    private List<MatchDto> randomPhaseMatches;
    private List<MatchDto> remainingPhaseMatches;
}
