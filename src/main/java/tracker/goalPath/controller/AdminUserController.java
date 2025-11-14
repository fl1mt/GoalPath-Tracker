package tracker.goalPath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tracker.goalPath.dto.UserDTO;
import tracker.goalPath.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users") // api/admin
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }
}
