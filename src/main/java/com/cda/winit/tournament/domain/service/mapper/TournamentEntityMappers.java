package com.cda.winit.tournament.domain.service.mapper;

import com.cda.winit.match.domain.dto.MatchDto;
import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.match.domain.service.mapper.MatchMapper;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.pagination.shared.abstract_classes.PaginableModel;
import com.cda.winit.pagination.shared.interfaces.IPaginableEntityToModelMappers;
import com.cda.winit.shared.domain.service.ImageUploadService;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import com.cda.winit.team.domain.dto.TeamTournamentDto;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.dto.TournamentCarouselDTO;
import com.cda.winit.tournament.domain.dto.TournamentCreationDto;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.tournament.domain.service.interfaces.ITournamentMapperService;
import com.cda.winit.tournament.infrastructure.repository.TournamentRepository;
import com.cda.winit.tournament.infrastructure.exception.TournamentServiceException;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentEntityMappers implements ITournamentMapperService {
    private final SportRepository sportRepository;
    private final TournamentRepository tournamentRepository;
    private final ImageUploadService imageUploadService;
    private final UserService userService;
    private final MatchMapper matchMapper;

    public Tournament ToCreationEntity(TournamentCreationDto model, User user) throws Exception {
        Optional<Tournament> optionalTournament = tournamentRepository.findByName(model.getName());

        if(optionalTournament.isPresent()) {
            throw new TournamentServiceException("Le nom de tournoi est déjà utilisé");
        }

        Tournament entity = new Tournament();

        this.setFormProperties(entity, model, user);
        this.setNonFormProperties(entity);

        return entity;
    }

    private void setFormProperties(Tournament entity, TournamentCreationDto model, User user) throws ParseException, IOException {
        this.setNonNullableProperties(entity, model, user);
        this.setNullableProperties(entity, model);
    }

    private void setNonNullableProperties(Tournament entity, TournamentCreationDto model, User user) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        entity.setName(model.getName());
        entity.setDate(df.parse(model.getDate()));
        entity.setPlace(model.getPlace());
        entity.setMaxNumberOfTeams(model.getMaxTeams());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCurrentNumberOfParticipants(0);
        entity.setUser(user);
    }

    private void setNullableProperties(Tournament entity, TournamentCreationDto model) throws IOException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Optional<Sport> sport = sportRepository.findByName(model.getSportName());
        entity.setSport(sport.orElseThrow());

        String imageUrl = model.getTournamentBanner() != null && !model.getTournamentBanner().isEmpty() ?
                imageUploadService.generateImageUrlAndSaveImage(model.getTournamentBanner()) :
                null;
        entity.setImageUrl(imageUrl);

        entity.setInscriptionLimitDate(df.parse(
                (model.getInscriptionLimitDate() != null) ?
                        model.getInscriptionLimitDate() :
                        model.getDate()));
    }

    private void setNonFormProperties(Tournament entity) {
        entity.setMatches(null);
        entity.setUpdatedAt(null);
    }

    public TournamentCarouselDTO entityToCarouselDTO(Tournament model) {
        TournamentCarouselDTO dto = new TournamentCarouselDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDate(model.getDate());
        dto.setPlace(model.getPlace());
        dto.setImageUrl(model.getImageUrl());
        dto.setCurrentNumberOfParticipants(model.getCurrentNumberOfParticipants());
        dto.setMaxNumberOfTeams(model.getMaxNumberOfTeams());
        dto.setSport(model.getSport().getName());
        return dto;
    }

    public TournamentDetailsDto entityToTournamentDetailsDTO(Tournament tournament, Boolean isOwner) throws Exception {
        TournamentDetailsDto dto = new TournamentDetailsDto();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setDate(tournament.getDate());
        dto.setPlace(tournament.getPlace());
        dto.setImageUrl(tournament.getImageUrl());
        dto.setCurrentNumberOfParticipants(tournament.getCurrentNumberOfParticipants());
        dto.setMaxNumberOfTeams(tournament.getMaxNumberOfTeams());
        dto.setSport(tournament.getSport().getName());
        dto.setInscriptionLimitDate(tournament.getInscriptionLimitDate());
        dto.setIsOwner(isOwner);
        dto.setIsGenerated(tournament.getIsGenerated());
        dto.setIsCanceled(tournament.getIsCanceled());

        List<Match> matches = tournament.getMatches();
        List<MatchDto> matchDtos = new ArrayList<>();

        for (Match match : matches) {
            MatchDto matchDto = matchMapper.mapMatchToDto(match);
            matchDtos.add(matchDto);
        }

        dto.setMatches(matchDtos);

        List<Team> teams = tournament.getTeams();
        List<TeamTournamentDto> teamTournamentDtos = new ArrayList<>();

        for (Team team : teams) {
            TeamTournamentDto teamDto = new TeamTournamentDto();
            teamDto.setId(team.getId());

            teamDto.setOwnerId(team.getUser().getId());
            teamDto.setName(team.getName());
            teamDto.setLeaderName(team.getUser().getFirstName());

            if (isOwner) {
                User user = userService.getCurrentUser().get();
                teamDto.setCurrentUser(user.getId());

            }
            teamTournamentDtos.add(teamDto);
        }

        dto.setTeams(teamTournamentDtos);

        return dto;
    }
}