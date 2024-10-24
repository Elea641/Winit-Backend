package com.cda.winit.team.domain.service;

import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import com.cda.winit.team.domain.dto.TeamUpdateDto;
import com.cda.winit.team.domain.service.interfaces.ITeamService;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.team.insfrastructure.exception.TeamServiceException;
import com.cda.winit.tournament.domain.service.TournamentService;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.team.domain.service.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamService implements ITeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final TeamMapper teamMapper;
    private final SportRepository sportRepository;
    private final TournamentService tournamentService;

    public List<TeamDto> findTeams() {
        try {
            Optional<User> currentUserOptional = userService.getCurrentUser();

            if (currentUserOptional.isPresent()) {
                User user = currentUserOptional.get();

                List<Team> teams = teamRepository.findByUserAndParticipation(user.getId());
                List<TeamDto> teamsDto = new ArrayList<>();

                for (Team team : teams) {
                    teamsDto.add(teamMapper.toDto(team));
                }
                return teamsDto;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception ex) {
            throw new TeamServiceException("Aucune équipe n'a été trouvé pour cet utilisateur");
        }
    }

    public TeamDto getTeamByTeamName(String teamName) {
        Team team = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new TeamServiceException("Aucune équipe n'a été trouvé avec ce nom: " + teamName));
        String ownerNames = teamRepository.findOwnerByTeamName(team.getName())[0];
        TeamDto teamDto = teamMapper.toDto(team);

        // Separate the string using coma and space as delimiter
        String[] separatedNames = ownerNames.split(",\\s*");

        String firstName = separatedNames[0];
        String lastName = separatedNames[1];

        teamDto.setOwnerFirstName(firstName);
        teamDto.setOwnerLastName(lastName);

        return teamDto;
    }

    public List<TeamDto> findTeamsBySport(String sportName) throws Exception {

        User user = userService.getCurrentUser().get();

        Optional<Sport> sportOptional = sportRepository.findByName(sportName);

        List<Team> teams = teamRepository.findByUserAndSportId(user, sportOptional.get().getId());

        List<TeamDto> teamsDto = new ArrayList<>();
        for (Team team : teams) {
            teamsDto.add(teamMapper.toDto(team));
        }

        return teamsDto;
    }

    public Team createTeam(TeamDto teamDto) throws Exception {
        Optional<User> currentUserOptional = userService.getCurrentUser();
        if (!currentUserOptional.isPresent()) {
            throw new TeamServiceException("L'utilisateur actuel n'est pas trouvé.");
        }

        User user = currentUserOptional.get();

        String teamName = teamDto.getName();

        if (teamRepository.existsByName(teamName)) {
            throw new TeamServiceException("Le nom de l'équipe est déjà pris.");
        }

        Team team = teamMapper.toEntity(teamDto, user);

        return teamRepository.save(team);
    }

    public void updateTeam(String teamName, TeamUpdateDto teamUpdateDto) {
        Team team = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new TeamServiceException("Aucune équipe n'a été trouvé avec ce nom: " + teamName));

        Optional<Team> existingTeam = teamRepository.findTeamByName(teamUpdateDto.getName());

        boolean isTeamInTournament = tournamentService.verifyTeamInTournament(team.getId());

        if (isTeamInTournament) {
            throw new TeamServiceException("Cette équipe est associée à un tournoi");
        }

        if (existingTeam.isPresent() && !Objects.equals(existingTeam.get().getId(), team.getId())
                && !team.getSport().equals(teamUpdateDto.getSport())) {
            throw new TeamServiceException("Le nom de l'équipe est déjà pris.");
        }

        Optional<Sport> optionalSport = sportRepository.findByName(teamUpdateDto.getSport());

        if (optionalSport.isPresent()) {
            int teamSize = team.getUsers().size();
            int maxPlayers = optionalSport.get().getNumberOfPlayers();

            if (teamSize > maxPlayers) {
                throw new TeamServiceException("Le nombre de joueurs dans votre équipe est trop important pour le sport choisi.");
            } else {
                team.setName(teamUpdateDto.getName());
                team.setSport(optionalSport.get());
                teamRepository.save(team);
            }
        } else {
            throw new TeamServiceException("Sport non trouvé avec le nom: " + teamUpdateDto.getSport());
        }
    }

    public void deleteTeam(String teamName) throws Exception {
        Team team = teamRepository.findTeamByName(teamName)
                .orElseThrow(() -> new TeamServiceException("Aucune équipe n'a été trouvé avec ce nom: " + teamName));

        boolean isTeamLead = this.verifyTeamLead(team);

        boolean isTeamInTournament = tournamentService.verifyTeamInTournament(team.getId());

        if (isTeamInTournament) {
            throw new TeamServiceException("Cette équipe est associée à un tournoi");
        }

        if (isTeamLead) {
            teamRepository.delete(team);
        } else {
            throw new TeamServiceException("Vous n'êtes pas autorisé à supprimer l'équipe.");
        }
    }

    public boolean verifyTeamLead(Team team) throws Exception {
        User user = userService.getCurrentUser().get();

        if (user != null && team != null) {
            return Objects.equals(user, team.getUser());
        } else {
            return false;
        }
    }
}