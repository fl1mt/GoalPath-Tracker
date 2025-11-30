package tracker.goalPath.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.service.SubtaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tasks/{taskId}/subtasks")
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
            @PathVariable UUID taskId,
            @Valid @RequestBody SubtaskDTO subtaskDTO,
            Authentication authentication
    ) {

        User user = getCurrentUser(authentication);
        SubtaskDTO createdSubtask = subtaskService.createSubtask(user.getId(), taskId, subtaskDTO);
        return ResponseEntity.ok(createdSubtask);
    }

    @GetMapping
    public ResponseEntity<List<SubtaskDTO>> getSubtasksByTask(@PathVariable UUID taskId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        List<SubtaskDTO> subtasks = subtaskService.getSubtasksByTask(user.getId(), taskId);
        return ResponseEntity.ok(subtasks);
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<SubtaskDTO> updateTask(
            @PathVariable UUID taskId,
            @PathVariable UUID subtaskId,
            @Valid @RequestBody SubtaskDTO subtaskDTO,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);

        SubtaskDTO updatedSubtask = subtaskService.updateSubtask(user.getId(), subtaskId, taskId, subtaskDTO);
        return ResponseEntity.ok(updatedSubtask);
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(
            @PathVariable UUID taskId,
            @PathVariable UUID subtaskId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        subtaskService.deleteSubtask(user.getId(), taskId, subtaskId);
        return ResponseEntity.noContent().build();
    }
}

