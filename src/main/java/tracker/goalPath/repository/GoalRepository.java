package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.goalPath.model.Goal;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);
}
