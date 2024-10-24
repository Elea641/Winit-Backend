package com.cda.winit.tournament.domain.service.interfaces;

import com.cda.winit.tournament.domain.dto.TournamentCarouselDTO;
import com.cda.winit.tournament.domain.dto.TournamentCreationDto;
import com.cda.winit.tournament.domain.dto.TournamentDetailsDto;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;

import java.io.IOException;
import java.text.ParseException;

public interface ITournamentMapperService {
    Tournament ToCreationEntity(TournamentCreationDto model, User user) throws Exception;

    private void setFormProperties(Tournament entity, TournamentCreationDto model, User user) throws ParseException, IOException {}

    private void setNonNullableProperties(Tournament entity, TournamentCreationDto model, User user) throws ParseException{}

    private void setNullableProperties(Tournament entity, TournamentCreationDto model) throws IOException, ParseException{}

    private void setNonFormProperties(Tournament entity){}

    TournamentCarouselDTO entityToCarouselDTO(Tournament model);

    TournamentDetailsDto entityToTournamentDetailsDTO(Tournament tournament, Boolean isOwner) throws Exception;

}
