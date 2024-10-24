package com.cda.winit.match.application;

import com.cda.winit.match.domain.dto.MatchUpdateDto;
import com.cda.winit.match.domain.dto.RankingDto;
import com.cda.winit.match.domain.service.MatchService;
import com.cda.winit.match.infrastructure.exception.MatchServiceException;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final TournamentService tournamentService;

    @GetMapping("/")
    public ResponseEntity<?> getRanking() {
        try {
            List<RankingDto> rankingDtos = matchService.findRanking();
            return ResponseEntity.ok().body(rankingDtos);
        } catch (MatchServiceException ex) {
            return ResponseEntity.badRequest().body("L'utilisateur ne possède pas d'équipe.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<?> updateMatch(@PathVariable Long matchId, @RequestBody MatchUpdateDto matchUpdateDto) {
        try {
            matchService.updatedMatch(matchId, matchUpdateDto);
            TournamentDetailsDto tournamentDetailsDto = tournamentService.getOneTournament(matchUpdateDto.getTournamentId());
            return ResponseEntity.ok().body(tournamentDetailsDto);
        } catch (MatchServiceException ex) {
            return ResponseEntity.badRequest().body("Erreur : " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Une erreur s'est produite : " + ex.getMessage());
        }
    }
}
