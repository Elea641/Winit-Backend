package com.cda.winit.tournament.domain.entity;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.pagination.shared.abstract_classes.PaginableEntity;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tournament extends PaginableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "inscription_limit_date", nullable = false)
    private Date inscriptionLimitDate;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "current_number_of_participants", nullable = false)
    private Integer currentNumberOfParticipants;

    @Column(name = "max_number_of_teams", nullable = false)
    private Integer maxNumberOfTeams;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_generated", nullable = false)
    private Boolean isGenerated = false;

    @Column(name = "is_canceled", nullable = false)
    private Boolean isCanceled = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tournament_team",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isGenerated='" + isGenerated + '\'' +
                '}';
    }
}