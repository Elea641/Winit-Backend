package com.cda.winit;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.entity.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchTest {

    private Match match;

    @BeforeEach
    public void setUp() { match = new Match(); }

    @Test
    public void testMatchId() {
        Long id = 1L;
        match.setId(id);
        assertEquals(id, match.getId());
    }

    @Test
    public void testMatchIsDraw() {
        Boolean isDraw = true;
        match.setIsDraw(isDraw);
        assertEquals(isDraw, match.getIsDraw());
    }

    @Test
    public void testMatchWinnerTeamId() {
        Long winnerTeamId = 12L;
        match.setWinnerTeamId(winnerTeamId);
        assertEquals(winnerTeamId, match.getWinnerTeamId());
    }

    @Test
    public void testMatchLoserTeamId() {
        Long loserTeamId = 14L;
        match.setLoserTeamId(loserTeamId);
        assertEquals(loserTeamId, match.getLoserTeamId());
    }

    @Test
    public void testMatchScoreTeam1() {
        Integer scoreTeam1 = 1;
        match.setScoreTeam1(scoreTeam1);
        assertEquals(scoreTeam1, match.getScoreTeam1());
    }

    @Test
    public void testMatchScoreTeam2() {
        Integer scoreTeam2 = 1;
        match.setScoreTeam2(scoreTeam2);
        assertEquals(scoreTeam2, match.getScoreTeam2());
    }

    @Test
    public void testMatchPhase() {
        String phase = "Finale";
        match.setPhase(phase);
        assertEquals(phase, match.getPhase());
    }

    @Test
    public void testMatchTournament() {
        Tournament tournament = new Tournament();
        match.setTournament(tournament);
        assertEquals(tournament, match.getTournament());
    }

    @Test
    public void testMatchTeams() {
        Team team1 = new Team();
        Team team2 = new Team();

        List<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        match.setTeams(teams);

        assertEquals(teams, match.getTeams());

        assertTrue(match.getTeams().contains(team1));
        assertTrue(match.getTeams().contains(team2));
    }
}
