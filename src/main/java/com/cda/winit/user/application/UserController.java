package com.cda.winit.user.application;


import com.cda.winit.user.domain.dto.UpdateUserRequest;
import com.cda.winit.user.domain.dto.UserDto;
import com.cda.winit.user.domain.dto.UserStatisticsDto;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.UserService;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAll() {
        try {
            return  ResponseEntity.ok().body(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/myself")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser() {
        try {
            Optional<User> currentUser = userService.getCurrentUser();
            return currentUser.map(user -> ResponseEntity.ok().body(user)).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserStatisticsDto> getUserStatistics() {
        try {
            UserStatisticsDto userStatisticsDto = userService.getUserStatistics();
            return ResponseEntity.ok().body(userStatisticsDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> edit(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        Optional<User> currentUser = userService.getCurrentUser();
        Long userId = userService.getCurrentUserId();

        if (currentUser.isPresent() && userId.equals(id)) {
            User updatedUser = currentUser.get();
            updatedUser.setFirstName(updateUserRequest.getFirstName());
            updatedUser.setLastName(updateUserRequest.getLastName());
            updatedUser.setEmail(updateUserRequest.getEmail());
            updatedUser.setCity(updateUserRequest.getCity());
            updatedUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) throws Exception {
        Optional<User> currentUser = userService.getCurrentUser();
        Long userId = userService.getCurrentUserId();

        if (currentUser.isPresent() && userId.equals(id)) {
            userRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
