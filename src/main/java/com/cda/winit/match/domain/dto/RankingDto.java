package com.cda.winit.match.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class RankingDto {
    private List<TopWinnerTeamCountDto> topWinnerTeamCountDtos;
    private  List<TopWinnerTeamDto> topWinnerTeamDtos;
}
