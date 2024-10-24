package com.cda.winit.match.domain.service.mapper;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.dto.RankingDto;
import com.cda.winit.match.domain.dto.TopWinnerTeamCountDto;
import com.cda.winit.match.domain.dto.TopWinnerTeamDto;
import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.match.domain.service.interfaces.IMatchMapperService;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchMapper implements IMatchMapperService {

    private final SportRepository sportRepository;

    public MatchDto mapMatchToDto(Match match) {
        MatchDto matchDto = new MatchDto();

        if (match.getTeams().size() >= 2) {
            matchDto.setId(match.getId());
            matchDto.setTeam1(match.getTeams().get(0).getName());
            matchDto.setTeam2(match.getTeams().get(1).getName());
            matchDto.setWinnerTeamId(match.getWinnerTeamId());
            matchDto.setLoserTeamId(match.getLoserTeamId());
            matchDto.setPhase(match.getPhase());
            matchDto.setScoreTeam1(match.getScoreTeam1());
            matchDto.setScoreTeam2(match.getScoreTeam2());
        }
        return matchDto;
    }

    public List<RankingDto> mapObjectsToRankingDto(List<Object[]> topWinnerTeamCounts, List<Object[]> topWinnerTeams) {
        List<RankingDto> rankingDtos = new ArrayList<>();

        List<TopWinnerTeamCountDto> topWinnerTeamCountDtos = new ArrayList<>();
        for (Object[] topWinnerTeamCount : topWinnerTeamCounts) {
            TopWinnerTeamCountDto topWinnerTeamCountDto = new TopWinnerTeamCountDto();

            topWinnerTeamCountDto.setId((Long) topWinnerTeamCount[0]);
            topWinnerTeamCountDto.setName((String) topWinnerTeamCount[1]);
            topWinnerTeamCountDto.setSport(sportRepository.findById((Long) topWinnerTeamCount[2]).get().getName());
            topWinnerTeamCountDto.setOwnerId((Long) topWinnerTeamCount[3]);
            topWinnerTeamCountDto.setCount((Long) topWinnerTeamCount[4]);

            topWinnerTeamCountDtos.add(topWinnerTeamCountDto);
        }

        List<TopWinnerTeamDto> topWinnerTeamDtos = new ArrayList<>();
        for (Object[] topWinnerTeam : topWinnerTeams) {
            TopWinnerTeamDto topWinnerTeamDto = new TopWinnerTeamDto();

            topWinnerTeamDto.setId((Long) topWinnerTeam[0]);
            topWinnerTeamDto.setName((String) topWinnerTeam[1]);
            topWinnerTeamDto.setSport(sportRepository.findById((Long) topWinnerTeam[2]).get().getName());
            topWinnerTeamDto.setOwnerId((Long) topWinnerTeam[3]);
            topWinnerTeamDto.setTournamentName((String) topWinnerTeam[4]);

            topWinnerTeamDtos.add(topWinnerTeamDto);
        }

        RankingDto rankingDto = new RankingDto();
        rankingDto.setTopWinnerTeamCountDtos(topWinnerTeamCountDtos);
        rankingDto.setTopWinnerTeamDtos(topWinnerTeamDtos);
        rankingDtos.add(rankingDto);

        return rankingDtos;
    }
}
