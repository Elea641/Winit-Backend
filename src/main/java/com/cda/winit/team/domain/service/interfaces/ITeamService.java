package com.cda.winit.team.domain.service.interfaces;

import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.dto.TeamUpdateDto;
import com.cda.winit.team.domain.entity.Team;

import java.util.List;

public interface ITeamService {

    List<TeamDto> findTeams();

    TeamDto getTeamByTeamName(String teamName);

    List<TeamDto> findTeamsBySport(String sportName) throws Exception;

    Team createTeam(TeamDto teamDto) throws Exception;

    void updateTeam(String teamName, TeamUpdateDto teamUpdateDto) throws Exception;

    void deleteTeam(String teamName) throws Exception;

    boolean verifyTeamLead(Team team) throws Exception;
}