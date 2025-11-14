package tracker.goalPath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.model.Task;
import tracker.goalPath.service.TaskService;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.model.User;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/goals/{goalId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @PathVariable Long goalId,
            @Valid @RequestBody TaskDTO taskDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        TaskDTO createdTask = taskService.createTask(user.getId(), goalId, taskDto);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksByGoal(@PathVariable Long goalId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<TaskDTO> tasks = taskService.getTasksByGoal(user.getId(), goalId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskDTO taskDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        TaskDTO updatedTask = taskService.updateTask(user.getId(), taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        taskService.deleteTask(user.getId(), taskId);
        return ResponseEntity.noContent().build();
    }
}