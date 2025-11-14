package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.goalPath.model.Subtask;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskId(Long taskId);
}
