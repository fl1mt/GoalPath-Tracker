package tracker.goalPath.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.service.TaskService;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.model.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/goals/{goalId}/tasks")
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
            @PathVariable UUID goalId,
            @Valid @RequestBody TaskDTO taskDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        TaskDTO createdTask = taskService.createTask(user.getId(), goalId, taskDto);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksByGoal(@PathVariable UUID goalId, Authentication authentication,
    @RequestParam(required = false) String status, @RequestParam(required = false) String query) {
        User user = getCurrentUser(authentication);
        List<TaskDTO> tasks = taskService.getTasksByGoal(status, query, user.getId(), goalId);
        return ResponseEntity.ok(tasks);
    }
}