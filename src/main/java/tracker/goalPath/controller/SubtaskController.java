package tracker.goalPath.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.service.SubtaskService;

import java.util.List;

@RestController
@RequestMapping("/goals/{goalId}/tasks/{taskId}/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final UserRepository userRepository;

    public SubtaskController(SubtaskService subtaskService, UserRepository userRepository) {
        this.subtaskService = subtaskService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @PostMapping
    public ResponseEntity<SubtaskDTO> createSubtask(
            @PathVariable Long taskId, @PathVariable Long goalId,
            @Valid @RequestBody SubtaskDTO subtaskDTO,
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);
        SubtaskDTO createdSubtask = subtaskService.createSubtask(user.getId(), goalId, taskId, subtaskDTO);
        return ResponseEntity.ok(createdSubtask);
    }

    @GetMapping
    public ResponseEntity<List<SubtaskDTO>> getSubtasksByTask(@PathVariable Long goalId,
                                                              @PathVariable Long taskId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        List<SubtaskDTO> subtasks = subtaskService.getSubtasksByTask(user.getId(), goalId, taskId);
        return ResponseEntity.ok(subtasks);
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<SubtaskDTO> updateTask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            @Valid @RequestBody SubtaskDTO subtaskDTO,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);

        SubtaskDTO updatedSubtask = subtaskService.updateSubtask(user.getId(), subtaskId, taskId, goalId, subtaskDTO);
        return ResponseEntity.ok(updatedSubtask);
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(
            @PathVariable Long goalId,
            @PathVariable Long taskId,
            @PathVariable Long subtaskId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        subtaskService.deleteSubtask(user.getId(), goalId, taskId, subtaskId);
        return ResponseEntity.noContent().build();
    }
}

