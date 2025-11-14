package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.goalPath.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByGoalId(Long goalId);
}
