package com.cda.winit.admin.application;

import com.cda.winit.admin.domain.dto.AdminUserDto;
import com.cda.winit.admin.domain.dto.EditUserRequest;
import com.cda.winit.admin.domain.service.AdminUserService;
import com.cda.winit.auth.domain.dto.RegisterRequest;
import com.cda.winit.auth.domain.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AuthService authService;

    @GetMapping("/")
    public List<AdminUserDto> getUsers() {
        return adminUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AdminUserDto getUser(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody RegisterRequest request,
                                                        HttpServletRequest httpRequest) throws Exception {
        return ResponseEntity.ok(authService.register(request, httpRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> editUser(@PathVariable Long id, @RequestBody EditUserRequest request) {
        return adminUserService.editUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        return adminUserService.deleteUser(id);
    }
}
