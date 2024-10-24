package com.cda.winit.tournamentTeam.application;

import com.cda.winit.tournamentTeam.domain.dto.TournamentTeamDto;
import com.cda.winit.tournamentTeam.domain.service.TournamentTeamService;
import com.cda.winit.tournamentTeam.infrastructure.exception.TournamentTeamException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/tournaments/teams")
@RequiredArgsConstructor
public class TournamentTeamController {

    private final TournamentTeamService tournamentTeamService;

    @PostMapping("/")
    public ResponseEntity<?> addTeamToTournament(@RequestBody TournamentTeamDto tournamentTeamDto) {
        try {
            tournamentTeamService.addTeamToTournament(tournamentTeamDto);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "L'équipe a bien été enregistrée dans le tournoi"));
        } catch (TournamentTeamException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{tournamentId}/{teamName}")
    public ResponseEntity<?> deleteTeamFromTournament(@PathVariable String tournamentId, @PathVariable String teamName) {
        try {
            tournamentTeamService.deleteTeamFromTournament(tournamentId, teamName);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "L'équipe a bien été supprimé"));
        } catch (TournamentTeamException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}