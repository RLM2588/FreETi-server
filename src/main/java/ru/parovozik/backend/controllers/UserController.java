package ru.parovozik.backend.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parovozik.backend.service.TaskService;
import ru.parovozik.backend.service.UserService;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AuthService authService;
    private final TaskService taskService;
    private final UserService userService;

    public UserController(AuthService authService, TaskService taskService, UserService userService) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @PutMapping("/id")
    public ResponseEntity<UserAnswer> updateUser(@RequestBody UserRequest userRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (!Objects.equals(userRequest.login(), username)) return (ResponseEntity<UserAnswer>) ResponseEntity.badRequest();
        userService.updateAvatar(username, userRequest.avatar());
        userService.changeName(username, userRequest.username());
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/user")
    public ResponseEntity<UserAnswer> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUser(userDetails.getUsername()));
    }

    @GetMapping("/username")
    public ResponseEntity<List<UserAnswer>> getUserByPartName(@RequestBody String username) {
        try {
            List<UserAnswer> userAnswer = userService.getUsersByPartName(username);
            if (userAnswer == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(userAnswer);
        }
        catch (Exception ex) {
            return (ResponseEntity<List<UserAnswer>>) ResponseEntity.notFound();
        }
    }

    @GetMapping("/login")
    public ResponseEntity<List<UserAnswer>> getUserByPartUsername(@Param("login") String login) {
        try {
            List<UserAnswer> userAnswer = userService.getUsersByPartUsername(login);
            if (userAnswer == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(userAnswer);
        }
        catch (Exception ex) {
            return (ResponseEntity<List<UserAnswer>>) ResponseEntity.notFound();
        }
    }

    @GetMapping("/byIds")
    public ResponseEntity<List<UserAnswer>> getUsersByIds(@Param("ids") String ids) {
        return ResponseEntity.ok(
                Arrays.stream(ids.split(","))
                        .map(id -> userService.getUserById(Integer.parseInt(id)))
                        .filter(Objects::nonNull)
                        .toList());
    }



}
