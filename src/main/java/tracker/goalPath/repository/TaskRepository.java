package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tracker.goalPath.model.Goal;
import tracker.goalPath.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal.id = :goalId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY " +
            "CASE t.status " +
            "WHEN 'PROGRESS' THEN 1 " +
            "WHEN 'FINISH' THEN 2 " +
            "WHEN 'CANCELED' THEN 3 " +
            "ELSE 4 " +
            "END")
    List<Task> findFilteredAndSortedTasks(
            @Param("goalId") UUID goalId,
            @Param("status") String status
    );

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal.id = :goalId " +
            "AND lower(CAST(t.title AS text)) LIKE lower(CONCAT('%', :query, '%')) " +
            "ORDER BY " +
            "CASE t.status " +
            "WHEN 'PROGRESS' THEN 1 " +
            "WHEN 'FINISH' THEN 2 " +
            "WHEN 'CANCELED' THEN 3 " +
            "ELSE 4 " +
            "END")
    List<Task> searchTasksByQueryAndSort(
            @Param("goalId") UUID goalId,
            @Param("query") String query
    );
    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.goal.user.id = :userId")
    Optional<Task> findByIdAndUserId(@Param("taskId") UUID taskId, @Param("userId") Long userId);
}
