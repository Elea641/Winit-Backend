package com.cda.winit.tournamentTeam.domain.service.interfaces;

import com.cda.winit.tournamentTeam.domain.dto.TournamentTeamDto;

public interface ITournamentTeamService {

    void addTeamToTournament(TournamentTeamDto tournamentTeamDto) throws Exception;
    void deleteTeamFromTournament(String tournamentId, String teamName) throws Exception;
}