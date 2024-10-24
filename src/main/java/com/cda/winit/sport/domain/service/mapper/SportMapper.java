package com.cda.winit.sport.domain.service.mapper;

import com.cda.winit.sport.domain.dto.SportDto;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.domain.service.interfaces.ISportMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SportMapper implements ISportMapperService {

    public Sport toEntity(SportDto sportDto, String imageUrl) {

        Sport sport = new Sport();
        sport.setName(sportDto.getName());
        sport.setNumberOfPlayers(sportDto.getNumberOfPlayers());
        sport.setImageUrl(imageUrl);
        return sport;
    }
    public List<SportDto> convertToDtoList(List<Sport> sports) {
        List<SportDto> sportDtoList = new ArrayList<>();

        for (Sport sport : sports) {
            SportDto sportDto = new SportDto("", 0,"");
            sportDto.setName(sport.getName());
            sportDto.setImageUrl(sport.getImageUrl());
            sportDto.setNumberOfPlayers(sport.getNumberOfPlayers());
            sportDtoList.add(sportDto);
        }

        return sportDtoList;
    }
}