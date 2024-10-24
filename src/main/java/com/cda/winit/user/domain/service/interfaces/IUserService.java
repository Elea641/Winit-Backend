package com.cda.winit.user.domain.service.interfaces;

import com.cda.winit.user.domain.dto.UserDto;
import com.cda.winit.user.domain.dto.UserStatisticsDto;
import com.cda.winit.user.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserDto> getAllUsers() throws Exception;

    Optional<User> getCurrentUser() throws Exception;

    Long getCurrentUserId() throws Exception;

    UserStatisticsDto getUserStatistics() throws Exception;
}