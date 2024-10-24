package com.cda.winit.admin.domain.service.interfaces;

import com.cda.winit.admin.domain.dto.AdminUserDto;
import com.cda.winit.admin.domain.dto.EditUserRequest;
import com.cda.winit.user.domain.entity.User;

import java.util.List;

public interface IAdminUserMapper {
    List<AdminUserDto> convertToDtoList(List<User> users);

    AdminUserDto convertToDto(User user);

    void mapRequest(User user, EditUserRequest request);
}
