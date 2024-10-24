package com.cda.winit.team.domain.dto;

import com.cda.winit.member.domain.dto.MemberDto;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    private Long id;
    private String name;
    private String sport;
    private String leaderName;
    private Integer totalPlayers;
    private Integer teamMembersCount;
    private boolean isValidated;
    private Long ownerId;
    private List<MemberDto> members;
    private String ownerFirstName = "";
    private String ownerLastName = "";
}