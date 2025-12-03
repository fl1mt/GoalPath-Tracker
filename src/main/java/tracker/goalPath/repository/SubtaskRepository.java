package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tracker.goalPath.model.Subtask;
import tracker.goalPath.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {

    @Query("SELECT s FROM Subtask s " +
            "WHERE s.task.id = :taskId " +
            "AND (:isCompleted IS NULL OR s.completed = :isCompleted) " +
            "ORDER BY " +
            "CASE " +
            "WHEN s.completed = FALSE THEN 1 " +
            "ELSE 2 " +
            "END")
    List<Subtask> findFilteredAndSortedSubtasks(
            @Param("taskId") UUID taskId,
            @Param("isCompleted") Boolean isCompleted
    );

    @Query("SELECT s FROM Subtask s " +
            "WHERE s.task.id = :taskId " +
            "AND lower(CAST(s.title AS text)) LIKE lower(CONCAT('%', :query, '%')) " +
            "ORDER BY " +
            "CASE " +
            "WHEN s.completed = FALSE THEN 1 " +
            "ELSE 2 " +
            "END")
    List<Subtask> searchSubtasksByQueryAndSort(
            @Param("taskId") UUID taskId,
            @Param("query") String query
    );
    @Query("SELECT s FROM Subtask s WHERE s.id = :subtaskId AND s.task.goal.user.id = :userId")
    Optional<Subtask> findByIdAndUserId(@Param("subtaskId") UUID subtaskId, @Param("userId") Long userId);
} 
