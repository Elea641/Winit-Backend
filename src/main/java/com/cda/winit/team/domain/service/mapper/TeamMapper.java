package com.cda.winit.team.domain.service.mapper;

import com.cda.winit.member.domain.dto.MemberDto;
import com.cda.winit.sport.infrastructure.repository.SportRepository;
import com.cda.winit.team.domain.service.interfaces.ITeamMapperService;
import com.cda.winit.team.insfrastructure.repository.TeamRepository;
import com.cda.winit.team.insfrastructure.exception.TeamNameAlreadyExistsException;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.sport.domain.service.SportService;
import com.cda.winit.team.domain.dto.TeamDto;
import com.cda.winit.team.domain.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMapper implements ITeamMapperService {

    private final SportService sportService;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;

    public Team toEntity(TeamDto teamDto, User user) {
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new TeamNameAlreadyExistsException("Le nom de l'équipe est déjà pris");
        }

        Team team = new Team();
        team.setName(teamDto.getName());
        team.setSport(sportRepository.findByName(teamDto.getSport()).get());
        team.setUser(user);
        return team;
    }

    public TeamDto toDto(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setId(team.getId());
        teamDto.setName(team.getName());
        teamDto.setOwnerId(team.getUser().getId());
        teamDto.setSport(sportService.findSportNameById(team.getSport().getId()));
        teamDto.setTotalPlayers(sportService.findSportNumberOfPlayers(team.getSport().getId()));
        List<MemberDto> memberDtos = new ArrayList<>();

        for (User user : team.getUsers()) {
            memberDtos.add(new MemberDto(user.getId(), user.getFirstName(), user.getLastName()));
        }

        teamDto.setMembers(memberDtos);

        int teamMembersCount = memberDtos.size();
        teamDto.setTeamMembersCount(teamMembersCount);

        return teamDto;
    }
}