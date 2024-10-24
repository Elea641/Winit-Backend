package com.cda.winit.admin.domain.service.interfaces;

import com.cda.winit.admin.domain.dto.AdminSportDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IAdminSportService {
    List<AdminSportDto> getAllSports();

    AdminSportDto getSportById(Long id);

    ResponseEntity<Map<String, String>> createSport(String name, int numberOfPlayers, MultipartFile file);

    ResponseEntity<Map<String, String>> editSport(Long id, String name, int numberOfPlayers, MultipartFile file) throws IOException;

    ResponseEntity<Map<String, String>> deleteSport(Long id);
}
