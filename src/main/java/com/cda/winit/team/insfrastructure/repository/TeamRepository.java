package com.cda.winit.team.insfrastructure.repository;

import com.cda.winit.team.domain.entity.Team;
import com.cda.winit.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT DISTINCT t.* FROM team t LEFT JOIN user_team u ON t.id = u.team_id WHERE u.user_id = :userId OR t.owner_id = :userId", nativeQuery = true)
    List<Team> findByUserAndParticipation(Long userId);

    List<Team> findByUserAndSportId(User user, Long sportId);

    Optional<Team> findTeamByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT COUNT(u.user_id) FROM team t JOIN user_team u ON t.id = u.team_id WHERE t.id = :teamId", nativeQuery = true)
    int countUsersByTeamId(Long teamId);

    @Query(value = "SELECT u.first_name, u.last_name from team t join `user` u on t.owner_id = u.id where t.name = :teamName", nativeQuery = true)
    String[] findOwnerByTeamName(String teamName);
}