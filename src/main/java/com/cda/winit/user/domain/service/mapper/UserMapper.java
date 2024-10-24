package com.cda.winit.user.domain.service.mapper;

import com.cda.winit.user.domain.dto.UserDto;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.domain.service.interfaces.IUserMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper implements IUserMapperService {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setId(user.getId());
        return userDto;
    }
}