package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.model.Goal;
import tracker.goalPath.mapper.GoalMapper;
import tracker.goalPath.repository.GoalRepository;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
    }

    public GoalDTO createGoal(Long userId, GoalDTO goalDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Goal goal = goalMapper.toEntity(goalDto);
        goal.setUser(user);

        System.out.println("Creating goal for user: " + userId);

        Goal saved = goalRepository.save(goal);
        return goalMapper.toDTO(saved);
    }

    public List<GoalDTO> getGoalsByUser(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(goalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<GoalDTO> getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .map(goalMapper::toDTO);
    }

    public GoalDTO updateGoal(Long goalId, GoalDTO goalDto) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setDeadline(goalDto.getDeadline());
        goal.setUpdatedAt(goalDto.getUpdatedAt());

        Goal updated = goalRepository.save(goal);
        return goalMapper.toDTO(updated);
    }

    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }
}
