package com.cda.winit.match.infrastructure.repository;

import com.cda.winit.match.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query(value = "SELECT t.*, COUNT(*) AS count FROM `match` m JOIN team t ON t.id = m.winner_team_id WHERE m.phase = 'Finale' AND m.winner_team_id IS NOT NULL GROUP BY m.winner_team_id ORDER BY COUNT(*) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTopWinnerTeamCount();

    @Query(value = "SELECT te.*, tour.name FROM `match` m JOIN tournament tour ON tour.id = m.tournament_id JOIN team te ON te.id = m.winner_team_id WHERE m.phase = 'Finale' AND m.winner_team_id IS NOT NULL ORDER BY date DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTopWinnerTeam();
}
