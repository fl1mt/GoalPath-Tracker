package tracker.goalPath.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("api/tasks")
public class TaskDirectionController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskDirectionController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID taskId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        TaskDTO task = taskService.getTaskById(user.getId(), taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskDTO taskDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        TaskDTO updatedTask = taskService.updateTask(user.getId(), taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID taskId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        taskService.deleteTask(user.getId(), taskId);
        return ResponseEntity.noContent().build();
    }
}
