package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tracker.goalPath.model.Subtask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {
    List<Subtask> findByTaskId(UUID taskId);

    @Query("SELECT s FROM Subtask s WHERE s.id = :subtaskId AND s.task.goal.user.id = :userId")
    Optional<Subtask> findByIdAndUserId(@Param("subtaskId") UUID subtaskId, @Param("userId") Long userId);
} 
