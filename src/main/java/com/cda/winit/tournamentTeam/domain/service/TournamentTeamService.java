package com.cda.winit.tournamentTeam.domain.service;

import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.team.domain.service.TeamService;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.tournament.infrastructure.exception.TournamentServiceException;
import com.cda.winit.tournament.infrastructure.repository.TournamentRepository;
import com.cda.winit.tournamentTeam.domain.dto.TournamentTeamDto;
import com.cda.winit.tournamentTeam.domain.service.interfaces.ITournamentTeamService;
import com.cda.winit.tournamentTeam.infrastructure.exception.TournamentTeamException;
import com.cda.winit.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentTeamService implements ITournamentTeamService {

    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;

    public void addTeamToTournament(TournamentTeamDto tournamentTeamDto) throws Exception {
        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentTeamDto.getTournamentId());
        Tournament tournament = optionalTournament.get();

        Optional<Team> optionalTeam = teamRepository.findTeamByName(tournamentTeamDto.getTeamName());
        Team team = optionalTeam.get();

        boolean isTeamLead = teamService.verifyTeamLead(optionalTeam.get());

        if (isTeamLead) {
            if (tournament != null && team != null) {

                int maxNumberOfTeams = tournament.getMaxNumberOfTeams();

                int countMembers = tournamentRepository.countUsersById(tournament.getId());

                if (countMembers >= maxNumberOfTeams) {
                    throw new TournamentTeamException("Le nombre maximal d'équipe est atteint pour participer au tournoi.");
                }

                int maxParticipantsBySport = team.getSport().getNumberOfPlayers();
                int currentParticipants = teamRepository.countUsersByTeamId(team.getId());

                if (currentParticipants < maxParticipantsBySport) {
                    throw new TournamentTeamException("Le nombre joueur dans l'équipe ne correspond au nombre attendu pour ce tournoi");
                }

                boolean isInscription = tournament.getTeams().contains(team);

                if (isInscription) {
                    throw new TournamentTeamException("L'équipe est déjà inscrite à ce tournoi.");
                }

                for (User member : team.getUsers()) {
                    for (Team existingTeam : tournament.getTeams()) {
                        if (existingTeam.getUsers().contains(member)) {
                            throw new TournamentTeamException("Le joueur " + member.getFirstName() + " est déjà inscrit dans une autre équipe pour ce tournoi.");
                        }
                    }
                }

                tournament.getTeams().add(optionalTeam.get());
                int currentNumberOfParticipants = tournament.getCurrentNumberOfParticipants();

                currentNumberOfParticipants++;

                tournament.setCurrentNumberOfParticipants(currentNumberOfParticipants);

                tournamentRepository.save(tournament);
            } else {
                throw new TournamentTeamException("L'équipe ou le tournoi spécifié n'existe pas.");
            }
        } else {
            throw new TournamentTeamException("Vous n'êtes pas autorisé à ajouter un membre à cette équipe.");
        }
    }

    public void deleteTeamFromTournament(String tournamentId, String teamName) throws Exception {
        Optional<Team> optionalTeam = teamRepository.findTeamByName(teamName);
        Team team = optionalTeam.orElseThrow(() -> new TournamentTeamException("Équipe inconnue: " + teamName));

        boolean isTeamLead = teamService.verifyTeamLead(team);

        if(isTeamLead) {
            long tournamentIdConvert = Long.parseLong(tournamentId);

            Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentIdConvert);
            Tournament tournament = tournamentOptional.orElseThrow(() -> new TournamentTeamException("Tournoi inconnu: " + tournamentId));

            try {
                Date inscriptionLimitDate = tournament.getInscriptionLimitDate();
                LocalDate limitLocalDate = inscriptionLimitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (tournament.getIsGenerated()) {
                    throw new TournamentServiceException("La génération du tournoi a été confirmé");
                }

                if (limitLocalDate.isBefore(LocalDate.now()) || limitLocalDate.equals(LocalDate.now())) {
                    throw new TournamentServiceException("La date d'inscription est passée");
                }

                tournament.getTeams().remove(team);
                tournament.setCurrentNumberOfParticipants(tournament.getCurrentNumberOfParticipants() - 1);

                tournamentRepository.save(tournament);
            } catch (Exception e) {
                throw new TournamentTeamException(e.getMessage());
            }
        } else {
            throw new TournamentTeamException("Vous n'êtes pas autorisé supprimer cette équipe du tournoi");
        }
    }
}