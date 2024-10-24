package com.cda.winit;

import com.cda.winit.sport.domain.entity.Sport;
import com.cda.winit.tournament.domain.entity.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SportTest {

    private Sport sport;

    @BeforeEach
    public void setUp() {
        sport = new Sport();
    }

    @Test
    public void testSportId() {
        Long id = 1L;
        sport.setId(id);
        assertEquals(id, sport.getId());
    }

    @Test
    public void testSportName() {
        String name = "Volley";
        sport.setName(name);
        assertEquals(name, sport.getName());
    }

    @Test
    public void testSportImageUrl() {
        String imageUrl = "image";
        sport.setImageUrl(imageUrl);
        assertEquals(imageUrl, sport.getImageUrl());
    }

    @Test
    public void testSportNumberOfPlayers() {
        int numberOfPlayers = 6;
        sport.setNumberOfPlayers(numberOfPlayers);
        assertEquals(numberOfPlayers, sport.getNumberOfPlayers());
    }

    @Test
    public void testSportTournaments() {
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();

        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(tournament1);
        tournaments.add(tournament2);

        sport.setTournaments(tournaments);

        assertEquals(tournaments, sport.getTournaments());

        assertTrue(sport.getTournaments().contains(tournament1));
        assertTrue(sport.getTournaments().contains(tournament2));
    }
}
