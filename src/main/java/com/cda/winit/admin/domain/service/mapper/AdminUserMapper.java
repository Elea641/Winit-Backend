package com.cda.winit.admin.domain.service.mapper;

import com.cda.winit.admin.domain.dto.AdminUserDto;
import com.cda.winit.admin.domain.dto.EditUserRequest;
import com.cda.winit.admin.domain.service.interfaces.IAdminUserMapper;
import com.cda.winit.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserMapper implements IAdminUserMapper {

    public List<AdminUserDto> convertToDtoList(List<User> users) {
        List<AdminUserDto> adminUserDtoList = new ArrayList<>();

        for (User user: users) {
            adminUserDtoList.add(this.convertToDto(user));
        }
        return adminUserDtoList;
    }

    public AdminUserDto convertToDto(User user) {
        AdminUserDto adminUserDto = new AdminUserDto(1L, "", "", "", "", true, "", LocalDateTime.now(), LocalDateTime.now());
        adminUserDto.setId(user.getId());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        adminUserDto.setEmail(user.getEmail());
        adminUserDto.setCity(user.getCity());
        adminUserDto.setIsAcceptTerms(user.getIsAcceptTerms());
        adminUserDto.setRequiredRole(user.getRole());
        adminUserDto.setCreatedAt(user.getCreatedAt());
        adminUserDto.setUpdatedAt(user.getUpdatedAt());

        return adminUserDto;
    }

    public void mapRequest(User user, EditUserRequest request) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCity(request.getCity());
        user.setRole(request.getRequiredRole());
        user.setIsAcceptTerms(request.getIsAcceptTerms());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
