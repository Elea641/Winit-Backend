package com.cda.winit.tournament.domain.service.interfaces;

import com.cda.winit.tournament.domain.dto.TournamentCreationDto;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.dto.TournamentUpdateDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ITournamentService {

    List<Tournament> getAllTournaments();

    List<Tournament> getAllGenerated();

    List<Tournament> getAllOpen();

    TournamentDetailsDto getOneTournament(Long id) throws Exception;

    List<Tournament> getAllTournamentsByUser() throws Exception;

    List<Tournament> getTournamentsByFilter(
            String searchValue,
            String selectedSport,
            Boolean chronologicalFilter,
            Boolean showOnlyUpcomingTournaments,
            Boolean showNonFullTournaments
    );

    Sort sortByNameOrDate(Boolean chronologicalFilter);

    Long createTournament(TournamentCreationDto newTournamentDto) throws Exception;

    TournamentDetailsDto updateTournament(Long tournamentId, TournamentUpdateDto tournamentUpdateDto) throws Exception;

    void deleteTournament(String tournamentName) throws Exception;

    boolean verifyTeamInTournament(Long teamId);
}