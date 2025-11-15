package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.goalPath.model.Goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);

    Optional<Goal> findByIdAndUserId(Long goalId, Long userId);
}
