package com.cda.winit.admin.domain.service;

import com.cda.winit.admin.domain.dto.AdminUserDto;
import com.cda.winit.admin.domain.dto.EditUserRequest;
import com.cda.winit.admin.domain.service.interfaces.IAdminUserService;
import com.cda.winit.admin.domain.service.mapper.AdminUserMapper;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService implements IAdminUserService {

    private final UserRepository userRepository;
    private final AdminUserMapper adminUserMapper;

    public List<AdminUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return adminUserMapper.convertToDtoList(users);
    }

    public AdminUserDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return adminUserMapper.convertToDto(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public ResponseEntity<Map<String, String>> editUser(Long id, EditUserRequest request) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            adminUserMapper.mapRequest(user, request);
            userRepository.save(user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Map<String, String>> deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
