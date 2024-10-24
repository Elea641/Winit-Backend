package com.cda.winit.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class AdminSportDto {
    private Long id;
    private String name;
    private int numberOfPlayers;
    private MultipartFile imageFile;
    private String imageUrl;
    private int numberOfTeams;
    private int numberOfTournaments;
}
