package com.cda.winit.admin.domain.service;


import com.cda.winit.admin.domain.dto.AdminSportDto;
import com.cda.winit.admin.domain.service.interfaces.IAdminSportService;
import com.cda.winit.admin.domain.service.mapper.AdminSportMapper;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminSportService implements IAdminSportService {

    private final SportRepository sportRepository;
    private final AdminSportMapper adminSportMapper;

    public List<AdminSportDto> getAllSports() {
        List<Sport> sports = sportRepository.findAll();

        return adminSportMapper.convertToDtoList(sports);
    }

    public AdminSportDto getSportById(Long id) {
        Optional<Sport> optionalSport = sportRepository.findById(id);

        if (optionalSport.isPresent()) {
            Sport sport = optionalSport.get();
            return adminSportMapper.convertToDto(sport);
        } else {
            throw new RuntimeException("Sport not found with id: " + id);
        }
    }

    public ResponseEntity<Map<String, String>> createSport(String name, int numberOfPlayers, MultipartFile file) {
        if (sportRepository.findByName(name).isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Sport sport = new Sport();
            sportRepository.save(adminSportMapper.mapSportParams(sport, name, numberOfPlayers, file));

            Map<String, String> response = new HashMap<>();
            response.put("message", "Sport created successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Map<String, String>> editSport(Long id, String name, int numberOfPlayers, MultipartFile file) throws IOException {
        Optional<Sport> optionalSport = sportRepository.findById(id);

        if (optionalSport.isPresent()) {
            Sport sport = optionalSport.get();
            sportRepository.save(adminSportMapper.mapSportParams(sport, name, numberOfPlayers, file));
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sport updated successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Map<String, String>> deleteSport(Long id) {
        Optional<Sport> optionalSport = sportRepository.findById(id);

        if (optionalSport.isPresent()) {
            sportRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Sport deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
