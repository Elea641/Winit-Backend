package com.cda.winit.tournament.application;

import com.cda.winit.match.domain.service.MatchService;
import com.cda.winit.match.infrastructure.exception.MatchServiceException;
import com.cda.winit.tournament.domain.dto.TournamentCarouselDTO;
import com.cda.winit.tournament.domain.dto.TournamentCreationDto;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.dto.TournamentUpdateDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.tournament.domain.service.mapper.TournamentEntityMappers;
import com.cda.winit.tournament.domain.service.TournamentService;
import com.cda.winit.tournament.infrastructure.exception.TournamentNotFoundException;
import com.cda.winit.tournament.infrastructure.exception.TournamentServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final MatchService matchService;
    private final TournamentEntityMappers mapper;

    @GetMapping("/")
    public ResponseEntity<ArrayList<TournamentCarouselDTO>> getAll() {
        try {
            List<Tournament> tournaments = tournamentService.getAllTournaments();
            var tournamentsDtos = new ArrayList<TournamentCarouselDTO>();
            tournaments.forEach(tournament -> tournamentsDtos.add(mapper.entityToCarouselDTO(tournament)));
            return ResponseEntity.ok().body(tournamentsDtos);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/generated")
    public ResponseEntity<ArrayList<TournamentCarouselDTO>> getAllGeneratedTournaments() {
        try {
            List<Tournament> tournaments = tournamentService.getAllGenerated();
            var tournamentsDtos = new ArrayList<TournamentCarouselDTO>();
            tournaments.forEach(tournament -> tournamentsDtos.add(mapper.entityToCarouselDTO(tournament)));
            return ResponseEntity.ok().body(tournamentsDtos);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/open")
    public ResponseEntity<ArrayList<TournamentCarouselDTO>> getAllOpenTournaments() {
        try {
            List<Tournament> tournaments = tournamentService.getAllOpen();
            var tournamentsDtos = new ArrayList<TournamentCarouselDTO>();
            tournaments.forEach(tournament -> tournamentsDtos.add(mapper.entityToCarouselDTO(tournament)));
            return ResponseEntity.ok().body(tournamentsDtos);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ArrayList<TournamentCarouselDTO>> getAllTournamentsByUser() {
        try {
            List<Tournament> tournaments = tournamentService.getAllTournamentsByUser();
            var tournamentsDtos = new ArrayList<TournamentCarouselDTO>();
            tournaments.forEach(tournament -> tournamentsDtos.add(mapper.entityToCarouselDTO(tournament)));
            return ResponseEntity.ok().body(tournamentsDtos);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<?> getById(@PathVariable Long tournamentId) {
        try {
            TournamentDetailsDto tournamentDto = tournamentService.getOneTournament(tournamentId);
            return ResponseEntity.ok().body(tournamentDto);
        } catch (TournamentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex);
        }
    }

    @GetMapping("/filter")
    public ArrayList<TournamentCarouselDTO> getTournamentsByFilter(
            @RequestParam String searchValue,
            @RequestParam String selectedSport,
            @RequestParam Boolean chronologicalFilter,
            @RequestParam Boolean showOnlyUpcomingTournaments,
            @RequestParam Boolean showNonFullTournaments
    ) {
        List<Tournament> tournaments = tournamentService.getTournamentsByFilter(
                searchValue,
                selectedSport,
                chronologicalFilter,
                showOnlyUpcomingTournaments,
                showNonFullTournaments
        );
        var tournamentsDtos = new ArrayList<TournamentCarouselDTO>();
        tournaments.forEach(tournament -> tournamentsDtos.add(mapper.entityToCarouselDTO(tournament)));
        return tournamentsDtos;
    }

    @PostMapping("/")
    public ResponseEntity<?> createTournament(@ModelAttribute TournamentCreationDto tournamentCreationDto) throws Exception {
        try {
            Long tournamentId = tournamentService.createTournament(tournamentCreationDto);
            return ResponseEntity.ok().body(tournamentId);
        } catch (TournamentServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex);
        }
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<?> updateTournament(@PathVariable Long tournamentId, @RequestBody TournamentUpdateDto tournamentUpdateDto) {try {
            if (tournamentUpdateDto.getMatches() != null) {
                matchService.createMatch(tournamentUpdateDto.getMatches(), tournamentId);
            }
            TournamentDetailsDto tournamentDetailsDto = tournamentService.updateTournament(tournamentId, tournamentUpdateDto);
            return ResponseEntity.ok().body(tournamentDetailsDto);
        } catch (TournamentServiceException | MatchServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{tournamentName}")
    public ResponseEntity<?> deleteTournament(@PathVariable String tournamentName) {
        try {
            tournamentService.deleteTournament(tournamentName);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Le tournoi a bien été supprimé"));
        } catch (TournamentServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex);
        }
    }
}
