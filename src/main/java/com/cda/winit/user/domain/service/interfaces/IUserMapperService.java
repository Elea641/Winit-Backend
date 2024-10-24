package com.cda.winit.user.domain.service.interfaces;

import com.cda.winit.user.domain.dto.UserDto;
import com.cda.winit.user.domain.entity.User;

public interface IUserMapperService {

    UserDto toDto(User user);
}
