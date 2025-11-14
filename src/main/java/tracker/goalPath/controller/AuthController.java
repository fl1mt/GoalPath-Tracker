package tracker.goalPath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.AuthRequest;
import tracker.goalPath.dto.AuthResponse;
import tracker.goalPath.dto.RegisterRequest;
import tracker.goalPath.dto.UserDTO;
import tracker.goalPath.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
