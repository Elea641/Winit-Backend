package com.cda.winit.team.domain.service.interfaces;

import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.user.domain.entity.User;

public interface ITeamMapperService {
    Team toEntity(TeamDto teamDto, User user);

    TeamDto toDto(Team team);
}
