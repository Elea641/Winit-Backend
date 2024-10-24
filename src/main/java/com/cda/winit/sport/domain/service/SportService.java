package com.cda.winit.sport.domain.service;

import com.cda.winit.member.infrastructure.exception.MemberServiceException;
import com.cda.winit.sport.domain.dto.SportDto;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.domain.service.interfaces.ISportService;
import com.cda.winit.sport.domain.service.mapper.SportMapper;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import com.cda.winit.sport.infrastructure.exception.SportServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SportService implements ISportService {

    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    public List<SportDto> findAllSports() {
        List<Sport> sports = sportRepository.findAll();

        if (!sports.isEmpty()) {
            return sportMapper.convertToDtoList(sports);
        } else {
            throw new SportServiceException("Erreur lors de la récupération des sports.");
        }
    }

    public void saveSport(Sport sport) {
        sportRepository.save(sport);
    }

    public List<String> findAllSportNames() {
        ArrayList<String> names = new ArrayList<>();
        sportRepository.findAll().forEach(sport -> names.add(sport.getName()));

        if (!names.isEmpty()) {
            return names;
        } else {
            throw new SportServiceException("Erreur lors de la récupération des noms de sports.");
        }
    }

    public Long findSportIdByName(String sportName) {
        Optional<Sport> optionalSport = sportRepository.findByName(sportName);
        return optionalSport.map(Sport::getId).orElse(null);
    }

    public String findSportNameById(Long sportId) {
        Optional<Sport> sport = sportRepository.findById(sportId);
        if (sport.isPresent()) {
            return sport.get().getName();
        } else {
            throw new RuntimeException("Aucun sport trouvé avec cet identifiant: " + sportId);
        }
    }

    public int findSportNumberOfPlayers(Long sportId) {
        Optional<Sport> sport = sportRepository.findById(sportId);
        if (sport.isPresent()) {
            return sport.get().getNumberOfPlayers();
        } else {
            throw new RuntimeException("Aucun sport trouvé avec cet identifiant: " + sportId);
        }
    }

    public int getMaxParticipantsForSport(Long sportId) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new MemberServiceException("Aucun sport trouvé avec cet identifiant: " + sportId));

        return sport.getNumberOfPlayers();
    }
}