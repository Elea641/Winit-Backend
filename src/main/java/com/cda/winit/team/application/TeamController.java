package com.cda.winit.team.application;

import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.dto.TeamUpdateDto;
import com.cda.winit.team.domain.service.TeamService;
import com.cda.winit.team.insfrastructure.exception.ListTeamByUserAlreadyExistsException;
import com.cda.winit.team.insfrastructure.exception.TeamServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/")
    public ResponseEntity<?> getAllTeamsOfUser() {
        try {
            List<TeamDto> teams = teamService.findTeams();
            return ResponseEntity.ok().body(teams);
        } catch (ListTeamByUserAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body("L'utilisateur ne possède pas d'équipe.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{teamName}")
    public ResponseEntity<?> getTeamByName(@PathVariable String teamName) {
        try {
            TeamDto teamDto = teamService.getTeamByTeamName(teamName);
            if (teamDto != null) {
                return ResponseEntity.ok().body(teamDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (TeamServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sport/{sportName}")
    public ResponseEntity<?> getTeamsBySport(@PathVariable String sportName) {
        try {
            List<TeamDto> teams = teamService.findTeamsBySport(sportName);
            return ResponseEntity.ok().body(teams);
        } catch (TeamServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createTeam(@RequestBody TeamDto teamDto) {
        try {
            teamService.createTeam(teamDto);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "L'équipe a bien été enregistrée"));
        } catch (TeamServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{teamName}")
    public ResponseEntity<?> updateTeam(@PathVariable String teamName, @RequestBody TeamUpdateDto teamUpdate) {
        try {
            teamService.updateTeam(teamName, teamUpdate);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "L'équipe a bien été mise à jour"));
        } catch (TeamServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{teamName}")
    public ResponseEntity<?> deleteTeam(@PathVariable String teamName) {
        try {
            teamService.deleteTeam(teamName);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "L'équipe a bien été supprimée"));
        } catch (TeamServiceException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}