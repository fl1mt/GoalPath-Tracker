package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.model.Goal;
import tracker.goalPath.mapper.GoalMapper;
import tracker.goalPath.repository.GoalRepository;
import tracker.goalPath.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final DataAuthorizationService dataAuthService;

    public GoalService(GoalRepository goalRepository, GoalMapper goalMapper, DataAuthorizationService dataAuthService) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
        this.dataAuthService = dataAuthService;
    }

    public GoalDTO createGoal(Long userId, GoalDTO goalDto) {

        User user = dataAuthService.checkUserData(userId);

        Goal goal = goalMapper.toEntity(goalDto);
        goal.setUser(user);

        Goal saved = goalRepository.save(goal);
        return goalMapper.toDTO(saved);
    }

    public List<GoalDTO> getGoalsByUser(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(goalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GoalDTO getGoalById(Long goalId, Long userId) {
        Goal goal = dataAuthService.checkGoalOwnership(goalId, userId);
        return goalMapper.toDTO(goal);
    }

    public GoalDTO updateGoal(Long goalId, Long userId, GoalDTO goalDto) {
        Goal goal = dataAuthService.checkGoalOwnership(goalId, userId);

        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setDeadline(goalDto.getDeadline());
        goal.setUpdatedAt(goalDto.getUpdatedAt());

        Goal updated = goalRepository.save(goal);
        return goalMapper.toDTO(updated);
    }

    public void deleteGoal(Long goalId, Long userId) {
        dataAuthService.checkGoalOwnership(goalId, userId);
        goalRepository.deleteById(goalId);
    }
}
