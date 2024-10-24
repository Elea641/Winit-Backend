package com.cda.winit.match.domain.service.interfaces;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.dto.RankingDto;
import com.cda.winit.match.domain.entity.Match;

import java.util.List;

public interface IMatchMapperService {
    MatchDto mapMatchToDto(Match match);

    List<RankingDto> mapObjectsToRankingDto(List<Object[]> topWinnerTeamCounts, List<Object[]> topWinnerTeams);
}
