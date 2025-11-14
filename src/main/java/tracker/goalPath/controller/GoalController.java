package tracker.goalPath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.service.GoalService;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.model.User;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserRepository userRepository;

    public GoalController(GoalService goalService, UserRepository userRepository) {
        this.goalService = goalService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @GetMapping
    public ResponseEntity<List<GoalDTO>> getUserGoals(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<GoalDTO> goals = goalService.getGoalsByUser(user.getId());
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable Long goalId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        return goalService.getGoalById(goalId)
                .filter(goal -> goal.getUserId().equals(user.getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GoalDTO> createGoal(
            @Valid @RequestBody GoalDTO goalDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);
        GoalDTO createdGoal = goalService.createGoal(user.getId(), goalDto);
        return ResponseEntity.ok(createdGoal);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<GoalDTO> updateGoal(
            @PathVariable Long goalId,
            @Valid @RequestBody GoalDTO goalDto,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);

        GoalDTO existingGoal = goalService.getGoalById(goalId)
                .filter(goal -> goal.getUserId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        GoalDTO updatedGoal = goalService.updateGoal(goalId, goalDto);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @PathVariable Long goalId,
            Authentication authentication
    ) {
        User user = getCurrentUser(authentication);

        GoalDTO goal = goalService.getGoalById(goalId)
                .filter(g -> g.getUserId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }
}