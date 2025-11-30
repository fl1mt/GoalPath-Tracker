package tracker.goalPath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.goalPath.model.Goal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByUserId(Long userId);

    Optional<Goal> findByIdAndUserId(UUID goalId, Long userId);
}
