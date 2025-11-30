package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tracker.goalPath.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByGoalId(UUID goalId);
    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.goal.user.id = :userId")
    Optional<Task> findByIdAndUserId(@Param("taskId") UUID taskId, @Param("userId") Long userId);
}
