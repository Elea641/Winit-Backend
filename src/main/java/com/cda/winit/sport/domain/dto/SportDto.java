package com.cda.winit.sport.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SportDto {
    private String name;
    private int numberOfPlayers;
    private String imageUrl;
}
