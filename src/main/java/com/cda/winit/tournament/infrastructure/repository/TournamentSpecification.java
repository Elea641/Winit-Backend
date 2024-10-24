package com.cda.winit.tournament.infrastructure.repository;

import com.cda.winit.tournament.domain.entity.Tournament;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TournamentSpecification {

    public static Specification<Tournament> filterTournament(
            String searchValue,
            String selectedSport,
            Boolean showOnlyUpcomingTournaments,
            Boolean showNonFullTournaments
    ) {
        return (root, query, criteriaBuilder) -> {
            Predicate namePredicate =
                    criteriaBuilder.like(root.get("name"), StringUtils.isBlank(searchValue)
                            ? likePattern("") : likePattern(searchValue));
            Predicate placePredicate =
                    criteriaBuilder.like(root.get("place"), StringUtils.isBlank(searchValue)
                            ? likePattern("") : likePattern(searchValue));

            Predicate combinedPredicate = criteriaBuilder.or(namePredicate, placePredicate);

            if (!StringUtils.isBlank(selectedSport)) {
                Predicate sportPredicate = criteriaBuilder.equal(root.get("sport").get("name"), selectedSport);
                combinedPredicate = criteriaBuilder.and(combinedPredicate, sportPredicate);
            }

            if (showOnlyUpcomingTournaments) {
                Predicate showOnlyUpcomingTournamentsPredicate = criteriaBuilder.greaterThan(root.get("date"), LocalDateTime.now());
                combinedPredicate = criteriaBuilder.and(combinedPredicate, showOnlyUpcomingTournamentsPredicate);
            }

            if (showNonFullTournaments) {
                Predicate showNonFullTournamentsPredicate = criteriaBuilder.lessThan(root.get("currentNumberOfParticipants"), root.get("maxNumberOfTeams"));
                combinedPredicate = criteriaBuilder.and(combinedPredicate, showNonFullTournamentsPredicate);
            }

            return combinedPredicate;
        };
    }

    private static String likePattern(String value) {
        return "%" + value + "%";
    }
}