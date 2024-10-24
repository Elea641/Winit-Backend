package com.cda.winit.admin.domain.service.interfaces;

import com.cda.winit.admin.domain.dto.AdminSportDto;
import com.cda.winit.sport.domain.entity.Sport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IAdminSportMapper {

    List<AdminSportDto> convertToDtoList(List<Sport> sports);

    AdminSportDto convertToDto(Sport sport);

    Sport mapSportParams(Sport sport, String name, int numberOfPlayers, MultipartFile file) throws IOException;
}
