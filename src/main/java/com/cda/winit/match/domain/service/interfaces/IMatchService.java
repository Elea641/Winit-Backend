package com.cda.winit.match.domain.service.interfaces;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.dto.MatchUpdateDto;
import com.cda.winit.match.domain.dto.MatchesDto;
import com.cda.winit.match.domain.dto.RankingDto;
import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;

import java.util.List;

public interface IMatchService {

    List<RankingDto> findRanking();

    void createMatch(MatchesDto matchesDto, Long tournamentId) throws Exception;

    private void createMatchesForPhase(List<MatchDto> matches, Tournament tournament, User user){}

    void updatedMatch(Long matchId, MatchUpdateDto matchUpdateDto) throws Exception;

    private void updateNextMatch(MatchUpdateDto matchUpdateDto, Match match) {}
    }
