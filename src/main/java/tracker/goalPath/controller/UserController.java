package tracker.goalPath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.UserDTO;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;
import tracker.goalPath.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @GetMapping ("/profile")
    public ResponseEntity<UserDTO> getUserData(Authentication authentication){
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDTO> updateUserUsername(@RequestBody UserDTO userDTO, Authentication authentication){
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(userService.updateUserUsername(user, userDTO));
    }
}
