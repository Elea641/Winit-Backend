package com.cda.winit;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeamTest {

    private Team team;

    @BeforeEach
    public void setUp() {
        team = new Team();
    }

    @Test
    public void testTeamId() {
        Long id = 1L;
        team.setId(id);
        assertEquals(id, team.getId());
    }

    @Test
    public void testTeamName() {
        String name = "The Team";
        team.setName(name);
        assertEquals(name, team.getName());
    }


    @Test
    public void testTeamSport() {
        Sport sport = new Sport();
        team.setSport(sport);
        assertEquals(sport, team.getSport());
    }

    @Test
    public void testTeamOwner() {
        User owner = new User();
        team.setUser(owner);
        assertEquals(owner, team.getUser());
    }

    @Test
    public void testTeamMatches() {
        Match match1 = new Match();
        Match match2 = new Match();

        List<Match> matches = new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        team.setMatches(matches);

        assertEquals(matches, team.getMatches());

        assertTrue(team.getMatches().contains(match1));
        assertTrue(team.getMatches().contains(match2));
    }

    @Test
    public void testTeamTournaments() {
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();

        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(tournament1);
        tournaments.add(tournament2);

        team.setTournaments(tournaments);

        assertEquals(tournaments, team.getTournaments());

        assertTrue(team.getTournaments().contains(tournament1));
        assertTrue(team.getTournaments().contains(tournament2));
    }

    @Test
    public void testTeamUsers() {
        User user1 = new User();
        User user2 = new User();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        team.setUsers(users);

        assertEquals(users, team.getUsers());

        assertTrue(team.getUsers().contains(user1));
        assertTrue(team.getUsers().contains(user2));
    }
}
