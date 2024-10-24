package com.cda.winit.user.domain.dto;

import com.cda.winit.tournament.domain.entity.Tournament;
import lombok.Data;

import java.util.List;

@Data
public class UserStatisticsDto {
    private Integer firstPlace;
    private Integer secondPlace;
    private Integer thirdPlace;
    private Integer participation;
    private Integer podium;
    private List<Tournament> lastTournaments;
    private List<Tournament> nextTournaments;
}
