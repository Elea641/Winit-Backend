package com.cda.winit;

import com.cda.winit.match.domain.entity.Match;
import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;
import jakarta.persistence.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentTest {

    private Tournament tournament;

    @BeforeEach
    public void setUp() {
        tournament = new Tournament();
    }

    @Test
    public void testTournamentId() {
        Long id = 1L;
        tournament.setId(id);
        assertEquals(id, tournament.getId());
    }

    @Test
    public void testTournamentName() {
        String name = "The Tournament";
        tournament.setName(name);
        assertEquals(name, tournament.getName());
    }

    @Test
    public void testTournamentDate() throws NoSuchFieldException {
        Field dateField = Tournament.class.getDeclaredField("date");
        Column columnAnnotation = dateField.getAnnotation(Column.class);
        assertNotNull(columnAnnotation, "The 'date' field should be annotated with @Column");
        assertFalse(columnAnnotation.nullable(), "The 'date' field should be non-nullable");
    }

    @Test
    public void testTournamentInscriptionLimitDate() throws NoSuchFieldException {
        Field inscriptionLimitDateField = Tournament.class.getDeclaredField("date");
        Column columnAnnotation = inscriptionLimitDateField.getAnnotation(Column.class);
        assertNotNull(columnAnnotation, "The 'date' field should be annotated with @Column");
        assertFalse(columnAnnotation.nullable(), "The 'date' field should be non-nullable");
    }

    @Test
    public void testTournamentPlace() {
        String place = "Bordeaux";
        tournament.setPlace(place);
        assertEquals(place, tournament.getPlace());
    }

    @Test
    public void testTournamentCurrentNumberOfParticipants() {
        int currentNumberOfParticipants = 6;
        tournament.setCurrentNumberOfParticipants(currentNumberOfParticipants);
        assertEquals(currentNumberOfParticipants, tournament.getCurrentNumberOfParticipants());
    }

    @Test
    public void testTournamentMaxNumberOfTeams() {
        int maxNumberOfTeams = 6;
        tournament.setMaxNumberOfTeams(maxNumberOfTeams);
        assertEquals(maxNumberOfTeams, tournament.getMaxNumberOfTeams());
    }

    @Test
    public void testTournamentImageUrl() {
        String imageUrl = "image";
        tournament.setImageUrl(imageUrl);
        assertEquals(imageUrl, tournament.getImageUrl());
    }

    @Test
    public void testTournamentCreatedAt() throws NoSuchFieldException {
        Field createdAtField = Tournament.class.getDeclaredField("date");
        Column columnAnnotation = createdAtField.getAnnotation(Column.class);
        assertNotNull(columnAnnotation, "The 'date' field should be annotated with @Column");
        assertFalse(columnAnnotation.nullable(), "The 'date' field should be non-nullable");
    }

    @Test
    public void testTournamentUpdatedAt() throws NoSuchFieldException {
        Field updatedAtField = Tournament.class.getDeclaredField("updatedAt");
        Column columnAnnotation = updatedAtField.getAnnotation(Column.class);
        assertNotNull(columnAnnotation, "The 'updatedAt' field should be annotated with @Column");
        assertEquals("updated_at", columnAnnotation.name(), "The column name should be 'updated_at'");
    }

    @Test
    public void testTournamentIsGenerated() {
        Boolean isGenerated = false;
        tournament.setIsGenerated(isGenerated);
        assertEquals(isGenerated, tournament.getIsGenerated());
    }

    @Test
    public void testTournamentIsCanceled() {
        Boolean isCanceled = false;
        tournament.setIsCanceled(isCanceled);
        assertEquals(isCanceled, tournament.getIsCanceled());
    }

    @Test
    public void testTournamentSport() {
        Sport sport = new Sport();
        tournament.setSport(sport);
        assertEquals(sport, tournament.getSport());
    }

    @Test
    public void testTournamentUser() {
        User user = new User();
        tournament.setUser(user);
        assertEquals(user, tournament.getUser());
    }

    @Test
    public void testTournamentMatches() {
        Match match1 = new Match();
        Match match2 = new Match();

        List<Match> matches = new ArrayList<>();
        matches.add(match1);
        matches.add(match2);

        tournament.setMatches(matches);

        assertEquals(matches, tournament.getMatches());

        assertTrue(tournament.getMatches().contains(match1));
        assertTrue(tournament.getMatches().contains(match2));
    }

    @Test
    public void testTournamentTeams() {
        Team team1 = new Team();
        Team team2 = new Team();

        List<Team> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        tournament.setTeams(teams);

        assertEquals(teams, tournament.getTeams());

        assertTrue(tournament.getTeams().contains(team1));
        assertTrue(tournament.getTeams().contains(team2));
    }
}
