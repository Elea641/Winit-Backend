package com.cda.winit.user.domain.service;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.dto.UserDto;
import com.cda.winit.user.domain.dto.UserStatisticsDto;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.interfaces.IUserService;
import com.cda.winit.user.domain.service.mapper.UserMapper;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TeamRepository teamRepository;

    public List<UserDto> getAllUsers() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            List<User> users = userRepository.findAll();

            if (users != null && !users.isEmpty()) {
                return users.stream()
                        .map(userMapper::toDto)
                        .collect(Collectors.toList());
            } else {
                throw new NoSuchElementException("No users found");
            }
        } else {
            throw new AccessDeniedException("User is not authenticated");
        }
    }

    public Optional<User> getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            Optional<User> currentUser = userRepository.findByEmail(currentUsername);

            if (currentUser.isPresent()) {
                return currentUser;
            } else {
                throw new UserPrincipalNotFoundException("User not found");
            }
        } else {
            throw new AccessDeniedException("User is not authenticated");
        }
    }

    public Long getCurrentUserId() throws Exception {
        Optional<User> currentUser = this.getCurrentUser();
        if (currentUser.isPresent()) {
            return currentUser.get().getId();
        } else {
            throw new UserPrincipalNotFoundException("User not found");
        }
    }

    public UserStatisticsDto getUserStatistics() throws Exception {
        Optional<User> currentUser = this.getCurrentUser();
        if (currentUser.isPresent()) {
            User Currentuser = currentUser.get();
            int firstPlace = 0;
            int secondPlace = 0;
            int thirdPlace = 0;
            int participation = 0;

            List<Team> teams = teamRepository.findByUserAndParticipation(Currentuser.getId());
            Set<Tournament> lastTournamentsSet = new HashSet<>();
            Set<Tournament> nextTournamentsSet = new HashSet<>();

            Date today = new Date();
            Set<Long> tournamentIds = new HashSet<>();

            for (Team team : teams) {
                List<User> users = team.getUsers();

                for (Tournament tournament : team.getTournaments()) {
                    boolean isGenerated = tournament.getIsGenerated();

                    if (tournament.getInscriptionLimitDate().before(today) && isGenerated) {
                        lastTournamentsSet.add(tournament);
                    }

                    if (tournament.getInscriptionLimitDate().after(today) && !isGenerated) {
                        nextTournamentsSet.add(tournament);
                    }
                }

                for (User user : users) {
                    if (Objects.equals(user.getId(), currentUser.get().getId())) {
                        for (Match match : team.getMatches()) {
                            if (!tournamentIds.contains(match.getTournament().getId())) {
                                participation++;
                                tournamentIds.add(match.getTournament().getId());
                            }
                            if ("Finale".equals(match.getPhase())) {
                                if (match.getWinnerTeamId() != null && match.getWinnerTeamId().equals(team.getId())) {
                                    if (match.getTeams().stream().anyMatch(t -> t.getUsers().contains(user))) {
                                        firstPlace++;
                                    }
                                }
                                if (match.getLoserTeamId() != null && match.getLoserTeamId().equals(team.getId())) {
                                    if (match.getTeams().stream().anyMatch(t -> t.getUsers().contains(user))) {
                                        secondPlace++;
                                    }
                                }
                            }
                            if ("Demi-finale".equals(match.getPhase())) {
                                if (match.getLoserTeamId() != null && match.getLoserTeamId().equals(team.getId())) {
                                    if (match.getTeams().stream().anyMatch(t -> t.getUsers().contains(user))) {
                                        thirdPlace++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<Tournament> lastTournaments = new ArrayList<>(lastTournamentsSet);
            List<Tournament> nextTournaments = new ArrayList<>(nextTournamentsSet);

            Collections.sort(lastTournaments, Comparator.comparing(Tournament::getInscriptionLimitDate).reversed());
            Collections.sort(nextTournaments, Comparator.comparing(Tournament::getInscriptionLimitDate));

            List<Tournament> threeLastTournaments = lastTournaments.subList(0, Math.min(lastTournaments.size(), 3));
            List<Tournament> threeNextTournaments = nextTournaments.subList(0, Math.min(nextTournaments.size(), 3));

            UserStatisticsDto userStatisticsDto = new UserStatisticsDto();
            userStatisticsDto.setFirstPlace(firstPlace);
            userStatisticsDto.setSecondPlace(secondPlace);
            userStatisticsDto.setThirdPlace(thirdPlace);
            userStatisticsDto.setParticipation(participation);
            userStatisticsDto.setPodium(firstPlace + secondPlace + thirdPlace);
            userStatisticsDto.setLastTournaments(threeLastTournaments);
            userStatisticsDto.setNextTournaments(threeNextTournaments);
            return userStatisticsDto;
        } else {
            throw new UserPrincipalNotFoundException("User not found");
        }
    }
}