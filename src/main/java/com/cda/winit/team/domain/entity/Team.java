package com.cda.winit.team.domain.entity;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "teams")
    private List<Match> matches;

    @ManyToMany(mappedBy = "teams")
    private List<Tournament> tournaments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
}