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
import ru.parovozik.backend.service.ContactService;
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
    private final ContactService contactService;

    public UserController(AuthService authService, TaskService taskService, UserService userService, ContactService contactService) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
        this.contactService = contactService;
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
    public ResponseEntity<List<UserAnswer>> getUserByPartUsername(@RequestParam("login") String login) {
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
    public ResponseEntity<List<UserAnswer>> getUsersByIds(@RequestParam("ids") String ids) {
        return ResponseEntity.ok(
                Arrays.stream(ids.split(","))
                        .map(id -> userService.getUserById(Integer.parseInt(id)))
                        .filter(Objects::nonNull)
                        .toList());
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactAnswer>> getContacts(@AuthenticationPrincipal UserDetails userDetails) {
//        User user = userService.getUserAsUser();

        return ResponseEntity.ok(contactService.getContactAndFriends(userDetails.getUsername()));
    }

    @PutMapping("/add_contact")
    public ResponseEntity<ContactAnswer> updateContact(@RequestBody ContactAnswer request, @AuthenticationPrincipal UserDetails userDetails) {
        int userId = userService.getUserAsUser(userDetails.getUsername()).getUserId();

        if (request.user1() != userId || request.user2() == userId) return (ResponseEntity<ContactAnswer>) ResponseEntity.badRequest();

        return ResponseEntity.ok(contactService.updateContact(request));
    }

    @DeleteMapping("/delete_contact")
    public ResponseEntity<Boolean> deleteContact(@RequestBody ContactAnswer request, @AuthenticationPrincipal UserDetails userDetails) {
        int userId = userService.getUserAsUser(userDetails.getUsername()).getUserId();

        if (request.user1() != userId || request.user2() == userId) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();
        return ResponseEntity.ok(contactService.deleteContact(request));
    }
}
