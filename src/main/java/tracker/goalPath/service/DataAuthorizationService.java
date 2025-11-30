package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import tracker.goalPath.exception.ResourceNotFoundException;
import tracker.goalPath.model.Goal;
import tracker.goalPath.model.Subtask;
import tracker.goalPath.model.Task;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.GoalRepository;
import tracker.goalPath.repository.SubtaskRepository;
import tracker.goalPath.repository.TaskRepository;
import tracker.goalPath.repository.UserRepository;

import java.util.UUID;

@Service
public class DataAuthorizationService {

    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserRepository userRepository;

    public DataAuthorizationService(GoalRepository goalRepository, TaskRepository taskRepository, SubtaskRepository subtaskRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.taskRepository = taskRepository;
        this.subtaskRepository = subtaskRepository;
        this.userRepository = userRepository;
    }

    public Goal checkGoalOwnership(UUID goalId, Long userId) {
        return goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found or access denied"));
    }

    public Task checkTaskOwnership(UUID taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
    }

    public Subtask checkSubtaskOwnership(UUID subtaskId, Long userId) {
        return subtaskRepository.findByIdAndUserId(subtaskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found or access denied"));
    }

    public User checkUserData(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found or access denied"));
    }
}