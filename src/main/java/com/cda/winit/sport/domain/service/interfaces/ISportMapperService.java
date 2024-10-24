package com.cda.winit.sport.domain.service.interfaces;

import com.cda.winit.sport.domain.dto.SportDto;
import com.cda.winit.sport.domain.entity.Sport;

import java.util.List;

public interface ISportMapperService {

    Sport toEntity(SportDto sportDto, String imageUrl);

    List<SportDto> convertToDtoList(List<Sport> sports);
}
