package com.cda.winit.tournament.infrastructure.repository;

import com.cda.winit.pagination.shared.interfaces.IPaginableEntityRepository;
import com.cda.winit.tournament.domain.entity.Tournament;
import com.cda.winit.user.domain.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament>, IPaginableEntityRepository<Tournament> {

    List<Tournament> findAll(Specification<Tournament> specification);

    @Query(value = "SELECT * FROM tournament WHERE is_generated = true AND is_canceled = false", nativeQuery = true)
    List<Tournament> findByIsGeneratedTrueAndiAndIsCanceledFalse();

    List<Tournament> findByInscriptionLimitDateAfterOrderByInscriptionLimitDate(LocalDateTime currentDate);

    List<Tournament> findAllByUser(Sort sort, User user);

    Optional<Tournament> findByName(String tournamentName);

    @Query(value = "SELECT COUNT(te.team_id) FROM tournament t JOIN tournament_team te ON te.tournament_id = t.id  WHERE t.id = :tournamentId", nativeQuery = true)
    int countUsersById(Long tournamentId);

    @Query(value ="SELECT * FROM tournament_team JOIN tournament ON tournament.id = tournament_team.tournament_id WHERE tournament_team.team_id = :teamId", nativeQuery = true)
    List<Tournament> findTournamentsByTeamsContaining(Long teamId);
}