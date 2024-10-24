package com.cda.winit.tournament.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TournamentCreationDto {

    @NonNull
    @JsonProperty("name")
    private String name;

    @NonNull
    @JsonProperty("place")
    private String place;

    @NonNull
    @JsonProperty("date")
    private String date;

    @NonNull
    @JsonProperty("sportName")
    private String sportName;

    @NonNull
    @JsonProperty("maxTeams")
    private int maxTeams;

    @Nullable
    @JsonProperty("inscriptionLimitDate")
    private String inscriptionLimitDate;

    @NonNull
    @JsonProperty("tournamentBanner")
    private MultipartFile tournamentBanner;
}
