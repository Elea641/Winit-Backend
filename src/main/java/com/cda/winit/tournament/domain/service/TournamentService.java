package com.cda.winit.tournament.domain.service;

import com.cda.winit.pagination.domain.mappers.PageMapper;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntityService;
import com.cda.winit.tournament.domain.dto.TournamentCarouselDTO;
import com.cda.winit.tournament.domain.dto.TournamentCreationDto;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.dto.TournamentUpdateDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.tournament.domain.service.interfaces.ITournamentService;
import com.cda.winit.tournament.domain.service.mapper.TournamentEntityMappers;
import com.cda.winit.tournament.domain.service.mapper.TournamentModelMappers;
import com.cda.winit.tournament.infrastructure.exception.TournamentNotFoundException;
import com.cda.winit.tournament.infrastructure.exception.TournamentServiceException;
import com.cda.winit.tournament.infrastructure.repository.TournamentRepository;
import com.cda.winit.tournament.infrastructure.repository.TournamentSpecification;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TournamentService extends PaginableEntityService<Tournament, TournamentCarouselDTO> implements ITournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentEntityMappers tournamentEntityMappers;
    private final UserService userService;

    public TournamentService(TournamentRepository tournamentRepository, TournamentEntityMappers tournamentEntityMappers, UserService userService) {
        super(tournamentRepository, new PageMapper<>(), new TournamentModelMappers());
        this.tournamentRepository = tournamentRepository;
        this.tournamentEntityMappers = tournamentEntityMappers;
        this.userService = userService;
    }

    public List<Tournament> getAllTournaments() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<Tournament> tournaments = tournamentRepository.findAll(sort);
        if (tournaments.isEmpty()) {
            return new ArrayList<>();
        }
        return tournaments;
    }

    public List<Tournament> getAllGenerated() {
        List<Tournament> tournaments = tournamentRepository.findByIsGeneratedTrueAndiAndIsCanceledFalse();
        if (tournaments.isEmpty()) {
            return new ArrayList<>();
        }
        return tournaments;
    }

    public List<Tournament> getAllOpen() {
        LocalDateTime currentDate = LocalDateTime.now();

        List<Tournament> tournaments = tournamentRepository.findByInscriptionLimitDateAfterOrderByInscriptionLimitDate(currentDate);
        if (tournaments.isEmpty()) {
            return new ArrayList<>();
        }
        return tournaments;
    }

    public TournamentDetailsDto getOneTournament(Long tournamentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

        if (!(authentication.getPrincipal() == "anonymousUser")) {
            Optional<User> optionalUser = userService.getCurrentUser();

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Boolean isOwner = Objects.equals(user.getId(), tournament.getUser().getId());

                return tournamentEntityMappers.entityToTournamentDetailsDTO(tournament, isOwner);
            } else {
                throw new TournamentServiceException("L'utilisateur n'a pas été trouvé");
            }
        } else {
            Boolean isOwner = false;
            return tournamentEntityMappers.entityToTournamentDetailsDTO(tournament, isOwner);
        }
    }

    public List<Tournament> getAllTournamentsByUser() throws Exception {
        Optional<User> optionalUser = userService.getCurrentUser();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Sort sort = Sort.by(Sort.Direction.DESC, "inscriptionLimitDate");

            List<Tournament> tournaments = tournamentRepository.findAllByUser(sort, user);

            if (tournaments.isEmpty()) {
                return new ArrayList<>();
            }
            return tournaments;
        }
        return null;
    }

    public List<Tournament> getTournamentsByFilter(
            String searchValue,
            String selectedSport,
            Boolean chronologicalFilter,
            Boolean showOnlyUpcomingTournaments,
            Boolean showNonFullTournaments
    ) {
        final Specification<Tournament> specification =
                TournamentSpecification.filterTournament(
                        searchValue,
                        selectedSport,
                        showOnlyUpcomingTournaments,
                        showNonFullTournaments
                );
        Sort sort = this.sortByNameOrDate(chronologicalFilter);

        return tournamentRepository.findAll(specification, sort);
    }

    public Sort sortByNameOrDate(Boolean chronologicalFilter) {
        if (chronologicalFilter) {
            return Sort.by(Sort.Direction.ASC, "date");
        } else {
            return Sort.by(Sort.Direction.ASC, "name");
        }
    }

    public Long createTournament(TournamentCreationDto newTournamentDto) throws Exception {
        User user = userService.getCurrentUser().get();
        Tournament tournamentCreated = tournamentRepository.save(tournamentEntityMappers.ToCreationEntity(newTournamentDto, user));
        return tournamentCreated.getId();
    }

    public TournamentDetailsDto updateTournament(Long tournamentId, TournamentUpdateDto tournamentUpdateDto) throws Exception {
        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentId);
        Tournament tournament = optionalTournament.orElseThrow(() -> new TournamentServiceException("Tournoi non trouvé: " + tournamentId));
        if (tournament != null) {
            tournament.setIsGenerated(tournamentUpdateDto.getIsGenerated());
            tournamentRepository.save(tournament);

            if (tournamentUpdateDto.getIsCanceled()) {
                tournament.setIsCanceled(true);
                tournamentRepository.save(tournament);
            }
            Optional<User> optionalUser = userService.getCurrentUser();

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Boolean isOwner = Objects.equals(user.getId(), tournament.getUser().getId());
                return tournamentEntityMappers.entityToTournamentDetailsDTO(tournament, isOwner);
            }
        } else {
            throw new TournamentServiceException("Tournoi non trouvé");
        }
        return null;
    }

    public void deleteTournament(String tournamentName) throws Exception {
        Optional<Tournament> optionalTournament = tournamentRepository.findByName(tournamentName);
        Tournament tournament = optionalTournament.orElseThrow(() -> new TournamentServiceException("Tournoi non trouvé: " + tournamentName));

        User user = userService.getCurrentUser().get();

        if (Objects.equals(user.getId(), tournament.getUser().getId())) {
            try {
                if (tournament.getIsGenerated()) {
                    throw new TournamentServiceException("Impossible de supprimer un tournoi confirmé");
                }

                Date inscriptionLimitDate = tournament.getInscriptionLimitDate();
                LocalDate limitLocalDate = inscriptionLimitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (!limitLocalDate.isAfter(LocalDate.now())) {
                    throw new TournamentServiceException("La date d'inscription est passée");
                }

                tournament.getTeams().clear();
                tournamentRepository.delete(tournament);
            } catch (Exception e) {
                throw new TournamentServiceException(e.getMessage());
            }
        } else {
            throw new TournamentServiceException("Vous n'êtes pas autorisé à supprimer ce tournoi");
        }
    }

    public boolean verifyTeamInTournament(Long teamId) {
        List<Tournament> tournaments = tournamentRepository.findTournamentsByTeamsContaining(teamId);
        return !tournaments.isEmpty();
    }
}