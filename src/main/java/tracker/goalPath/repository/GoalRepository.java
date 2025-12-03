package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tracker.goalPath.model.Goal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {

    @Query("SELECT g FROM Goal g " +
            "WHERE g.user.id = :userId " +
            "AND (:status IS NULL OR g.status = :status) " +
            "ORDER BY " +
            "CASE g.status " +
            "WHEN 'PROGRESS' THEN 1 " +
            "WHEN 'FINISH' THEN 2 " +
            "WHEN 'CANCELED' THEN 3 " +
            "ELSE 4 " +
            "END")
    List<Goal> findFilteredAndSortedGoals(
            @Param("userId") Long userId,
            @Param("status") String status
    );

    @Query("SELECT g FROM Goal g " +
            "WHERE g.user.id = :userId " +
            "AND lower(CAST(g.title AS text)) LIKE lower(CONCAT('%', :query, '%')) " +
            "ORDER BY " +
            "CASE g.status " +
            "WHEN 'PROGRESS' THEN 1 " +
            "WHEN 'FINISH' THEN 2 " +
            "WHEN 'CANCELED' THEN 3 " +
            "ELSE 4 " +
            "END")
    List<Goal> searchGoalsByQueryAndSort(
            @Param("userId") Long userId,
            @Param("query") String query
    );

    Optional<Goal> findByIdAndUserId(UUID goalId, Long userId);

}