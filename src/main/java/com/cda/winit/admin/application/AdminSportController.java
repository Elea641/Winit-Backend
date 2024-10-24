package com.cda.winit.admin.application;

import com.cda.winit.admin.domain.dto.AdminSportDto;
import com.cda.winit.admin.domain.service.AdminSportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/sports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminSportController {

    private final AdminSportService adminSportService;

    @GetMapping("/")
    public List<AdminSportDto> getSports() {
        return adminSportService.getAllSports();
    }

    @GetMapping("/{id}")
    public AdminSportDto getSport(@PathVariable Long id) {
        return adminSportService.getSportById(id);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createSport(
            @RequestParam("name") String name,
            @RequestParam("numberOfPlayers") int numberOfPlayers,
            @RequestParam("imageFile") MultipartFile file
    )  {
        return adminSportService.createSport(name, numberOfPlayers, file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> editSport(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("numberOfPlayers") int numberOfPlayers,
            @RequestParam("imageFile") MultipartFile file
    ) throws IOException {
        return adminSportService.editSport(id, name, numberOfPlayers, file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSport(@PathVariable Long id) {
        return adminSportService.deleteSport(id);
    }
}
