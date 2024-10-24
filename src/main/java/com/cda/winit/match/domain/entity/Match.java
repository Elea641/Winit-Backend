package com.cda.winit.match.domain.entity;

import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`match`")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_draw")
    private Boolean isDraw;

    @Column(name = "winner_team_id")
    private Long winnerTeamId;

    @Column(name = "loser_team_id")
    private Long loserTeamId;

    @Column(name = "score_team1")
    private Integer scoreTeam1;

    @Column(name = "score_team2")
    private Integer scoreTeam2;

    @Column(name = "phase")
    private String phase;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToMany
    @JoinTable(
            name = "match_team",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams = new ArrayList<>();

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + phase + '\'' +
                ", scoreTeam1='" + scoreTeam1 + '\'' +
                ", scoreTeam2='" + scoreTeam2 + '\'' +
                '}';
    }
}