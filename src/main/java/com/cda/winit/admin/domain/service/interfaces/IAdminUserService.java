package com.cda.winit.admin.domain.service.interfaces;

import com.cda.winit.admin.domain.dto.AdminUserDto;
import com.cda.winit.admin.domain.dto.EditUserRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IAdminUserService {
    List<AdminUserDto> getAllUsers();

    AdminUserDto getUserById(Long id);

    ResponseEntity<Map<String, String>> editUser(Long id, EditUserRequest request);

    ResponseEntity<Map<String, String>> deleteUser(Long id);
}
