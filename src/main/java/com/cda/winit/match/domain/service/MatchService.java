package com.cda.winit.match.domain.service;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.dto.MatchUpdateDto;
import com.cda.winit.match.domain.dto.MatchesDto;

import com.cda.winit.match.domain.dto.RankingDto;
import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.match.domain.service.interfaces.IMatchService;
import com.cda.winit.match.domain.service.mapper.MatchMapper;
import com.cda.winit.match.infrastructure.exception.MatchServiceException;
import com.cda.winit.match.infrastructure.repository.MatchRepository;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.tournament.infrastructure.repository.TournamentRepository;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService implements IMatchService {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final UserService userService;
    private final MatchMapper matchMapper;
    public List<RankingDto> findRanking() {
        List<Object[]> topWinnerTeamCount = matchRepository.findTopWinnerTeamCount();
        List<Object[]> topWinnerTeams = matchRepository.findTopWinnerTeam();

        List<RankingDto> rankingDtos = matchMapper.mapObjectsToRankingDto(topWinnerTeamCount, topWinnerTeams);

        if (topWinnerTeams != null && topWinnerTeamCount != null) {
            return rankingDtos;
        } else {
            throw new MatchServiceException("Aucun classement n'a été trouvé");
        }
    }

    public void createMatch(MatchesDto matchesDto, Long tournamentId) throws Exception {
        User user = userService.getCurrentUser().get();

        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentId);
        List<MatchDto> otherMatches = matchesDto.getOtherMatches();
        List<MatchDto> randomPhaseMatches = matchesDto.getRandomPhaseMatches();
        List<MatchDto> remainingPhaseMatches = matchesDto.getRemainingPhaseMatches();

        if(!optionalTournament.get().getIsGenerated()) {
            if (optionalTournament.isPresent() && Objects.equals(user.getId(), optionalTournament.get().getUser().getId())) {
                createMatchesForPhase(otherMatches, optionalTournament.get(), user);
                createMatchesForPhase(randomPhaseMatches, optionalTournament.get(), user);
                createMatchesForPhase(remainingPhaseMatches, optionalTournament.get(), user);
            } else {
                throw new MatchServiceException("Vous n'êtes pas autorisé à générer les équipes");
            }
        } else {
            throw new MatchServiceException("Le tournoi est déjà généré.");
        }
    }

    private void createMatchesForPhase(List<MatchDto> matches, Tournament tournament, User user) {
        for (MatchDto matchDto : matches) {
            String team1Name = matchDto.getTeam1();
            String team2Name = matchDto.getTeam2();

            Optional<Team> optionalTeam1 = teamRepository.findTeamByName(team1Name);
            Optional<Team> optionalTeam2 = teamRepository.findTeamByName(team2Name);

            Match match = new Match();
            match.setTournament(tournament);
            match.setPhase(matchDto.getPhase());
            match.setScoreTeam1(0);
            match.setScoreTeam2(0);

            if ("En attente".equals(team1Name) || "En attente".equals(team2Name)) {
                Optional<Team> optionalWaitingTeam = teamRepository.findTeamByName("En attente");
                Team waitingTeam;

                if (optionalWaitingTeam.isPresent()) {
                    waitingTeam = optionalWaitingTeam.get();
                } else {
                    waitingTeam = new Team();
                    waitingTeam.setName("En attente");
                    waitingTeam.setUser(user);
                    waitingTeam.setSport(tournament.getSport());

                    teamRepository.save(waitingTeam);
                }

                if (!optionalTeam1.isPresent()) {
                    match.getTeams().add(waitingTeam);
                } else {
                    match.getTeams().add(optionalTeam1.get());
                }

                if (!optionalTeam2.isPresent()) {
                    match.getTeams().add(waitingTeam);
                } else {
                    match.getTeams().add(optionalTeam2.get());
                }
            } else {
                optionalTeam1.ifPresent(team -> match.getTeams().add(team));
                optionalTeam2.ifPresent(team -> match.getTeams().add(team));
            }

            matchRepository.save(match);
        }
    }

    public void updatedMatch(Long matchId, MatchUpdateDto matchUpdateDto) throws Exception {
        Optional<User> optionalUser = userService.getCurrentUser();

        if (optionalUser.isPresent()) {
            Optional<Match> optionalMatch = matchRepository.findById(matchId);

            if(optionalMatch.isPresent()) {
                Match match = optionalMatch.get();

                match.setScoreTeam1(matchUpdateDto.getScoreTeam1());
                match.setScoreTeam2(matchUpdateDto.getScoreTeam2());

                if (match.getScoreTeam1() > match.getScoreTeam2()) {
                    match.setWinnerTeamId(match.getTeams().get(0).getId());
                    match.setLoserTeamId(match.getTeams().get(1).getId());
                } else {
                    match.setWinnerTeamId(match.getTeams().get(1).getId());
                    match.setLoserTeamId(match.getTeams().get(0).getId());
                }

                if (matchUpdateDto.getScoreTeam1().equals(matchUpdateDto.getScoreTeam2())) {
                    throw new MatchServiceException("Les scores ne peuvent être identiques pour ce match");
                }

                matchRepository.save(match);

                if (matchUpdateDto.getNextTeamInfo().getId() == null && matchUpdateDto.getNextTeamInfo().getTeam() == null) {
                } else {
                    this.updateNextMatch(matchUpdateDto, match);
                }
            } else {
                throw new MatchServiceException("Aucun match trouvé");
            }
        } else {
            throw new MatchServiceException("Aucun utilisateur trouvé");
        }
    }

    private void updateNextMatch(MatchUpdateDto matchUpdateDto, Match match) {
        Optional<Match> optionalNextMatch = matchRepository.findById(matchUpdateDto.getNextTeamInfo().getId());

        if(optionalNextMatch.isPresent()) {
            Match nextMatch = optionalNextMatch.get();
            for (Team team : nextMatch.getTeams()) {
                if ("En attente".equals(team.getName())) {
                    if (match.getWinnerTeamId() != null) {
                        nextMatch.getTeams().remove(team);
                        if (Objects.equals(match.getTeams().get(0).getId(), match.getWinnerTeamId())) {
                            nextMatch.getTeams().add(match.getTeams().get(0));
                            matchRepository.save(nextMatch);
                        } else {
                            nextMatch.getTeams().add(match.getTeams().get(1));
                            matchRepository.save(nextMatch);
                        }
                    } else {
                        throw new MatchServiceException("Aucun gagnant n'a été trouvé pour ce match");
                    }
                    break;
                }
            }
        } else {
            throw new MatchServiceException("Prochain match non trouvé");
        }
    }
}
