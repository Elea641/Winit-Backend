package com.cda.winit.sport.domain.service.interfaces;

import com.cda.winit.sport.domain.dto.SportDto;
import com.cda.winit.sport.domain.entity.Sport;

import java.util.List;

public interface ISportService {
    List<SportDto> findAllSports();

    void saveSport(Sport sport);

    List<String> findAllSportNames();

    Long findSportIdByName(String sportName);

    String findSportNameById(Long sportId);

    int findSportNumberOfPlayers(Long sportId);

    int getMaxParticipantsForSport(Long sportId);
}