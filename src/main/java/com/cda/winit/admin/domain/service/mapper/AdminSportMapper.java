package com.cda.winit.admin.domain.service.mapper;

import com.cda.winit.admin.domain.dto.AdminSportDto;
import com.cda.winit.admin.domain.service.interfaces.IAdminSportMapper;
import com.cda.winit.shared.domain.service.ImageUploadService;
import com.cda.winit.sport.domain.entity.Sport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSportMapper implements IAdminSportMapper {

    private final ImageUploadService imageUploadService;

    public List<AdminSportDto> convertToDtoList(List<Sport> sports) {
        List<AdminSportDto> adminSportDtoList = new ArrayList<>();

        for (Sport sport : sports) {
            adminSportDtoList.add(this.convertToDto(sport));
        }

        return adminSportDtoList;
    }

    public AdminSportDto convertToDto(Sport sport) {

        AdminSportDto adminSportDto = new AdminSportDto(1L,"", 0, null,"", 0, 0);
        adminSportDto.setId(sport.getId());
        adminSportDto.setName(sport.getName());
        adminSportDto.setImageUrl(sport.getImageUrl());
        adminSportDto.setNumberOfPlayers(sport.getNumberOfPlayers());
        adminSportDto.setNumberOfTournaments(sport.getTournaments().size());

        return adminSportDto;
    }

    public Sport mapSportParams(Sport sport, String name, int numberOfPlayers, MultipartFile file) throws IOException {

        String imageUrl = imageUploadService.generateImageUrlAndSaveImage(file);
        sport.setImageUrl(imageUrl);
        sport.setName(name);
        sport.setNumberOfPlayers(numberOfPlayers);

        return sport;
    }
}
