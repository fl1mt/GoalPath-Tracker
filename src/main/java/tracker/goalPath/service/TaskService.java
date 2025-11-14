package tracker.goalPath.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.mapper.TaskMapper;
import tracker.goalPath.model.Goal;
import tracker.goalPath.model.Task;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.GoalRepository;
import tracker.goalPath.repository.TaskRepository;
import tracker.goalPath.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, GoalRepository goalRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public TaskDTO createTask(Long userId, Long goalId, TaskDTO taskDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Goal goal = goalRepository.findById(goalId)
                .filter(g -> g.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Goal not found or does not belong to user"));

        Task task = taskMapper.toEntity(taskDTO);

        task.setGoal(goal);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> getTasksByGoal(Long userId, Long goalId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Goal goal = goalRepository.findById(goalId)
                .filter(g -> g.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Goal not found or does not belong to user"));

        List<Task> tasks = taskRepository.findByGoalId(goalId);

        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long userId, Long taskId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getGoal().getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or does not belong to user"));

        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long userId, Long taskId, TaskDTO taskDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Task existingTask = taskRepository.findById(taskId)
                .filter(t -> t.getGoal().getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or does not belong to user"));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDeadline(taskDTO.getDeadline());

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toDTO(updatedTask);
    }

    public void deleteTask(Long userId, Long taskId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getGoal().getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found or does not belong to user"));

        taskRepository.delete(task);
    }
}
