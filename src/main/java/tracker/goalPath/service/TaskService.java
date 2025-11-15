package tracker.goalPath.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.mapper.TaskMapper;
import tracker.goalPath.model.Goal;
import tracker.goalPath.model.Task;
import tracker.goalPath.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final DataAuthorizationService authService;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, DataAuthorizationService authService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.authService = authService;
    }

    public TaskDTO createTask(Long userId, Long goalId, TaskDTO taskDTO) {

        Goal goal = authService.checkGoalOwnership(goalId, userId);
        Task task = taskMapper.toEntity(taskDTO);

        task.setGoal(goal);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> getTasksByGoal(Long userId, Long goalId) {

        authService.checkGoalOwnership(goalId, userId);

        List<Task> tasks = taskRepository.findByGoalId(goalId);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long userId, Long taskId) {

        Task task = authService.checkTaskOwnership(taskId, userId);
        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long userId, Long taskId, TaskDTO taskDTO) {

        Task existingTask = authService.checkTaskOwnership(taskId, userId);

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDeadline(taskDTO.getDeadline());

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toDTO(updatedTask);
    }

    public void deleteTask(Long userId, Long taskId) {

        Task task = authService.checkTaskOwnership(taskId, userId);
        taskRepository.delete(task);
    }
}
