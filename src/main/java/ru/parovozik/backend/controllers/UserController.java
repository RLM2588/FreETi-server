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
        try {
            String username = userDetails.getUsername();
            if (!Objects.equals(userRequest.login(), username)) return ResponseEntity.badRequest().build();
            userService.updateAvatar(username, userRequest.avatar());
            userService.changeName(username, userRequest.username());
            return ResponseEntity.ok(userService.getUser(username));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserAnswer> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(userService.getUser(userDetails.getUsername()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/username")
    public ResponseEntity<List<UserAnswer>> getUserByPartName(@RequestParam("username") String username) {
        try {
            List<UserAnswer> userAnswer = userService.getUsersByPartName(username);
            if (userAnswer == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(userAnswer);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/login")
    public ResponseEntity<List<UserAnswer>> getUserByPartUsername(@RequestParam("login") String login) {
        try {
            List<UserAnswer> userAnswer = userService.getUsersByPartUsername(login);
            if (userAnswer == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(userAnswer);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byIds")
    public ResponseEntity<List<UserAnswer>> getUsersByIds(@RequestParam("ids") String ids) {
        try {
            return ResponseEntity.ok(
                    Arrays.stream(ids.split(","))
                            .map(id -> userService.getUserById(Integer.parseInt(id)))
                            .filter(Objects::nonNull)
                            .toList());
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactAnswer>> getContacts(@AuthenticationPrincipal UserDetails userDetails) {
//        User user = userService.getUserAsUser();
        try {
            return ResponseEntity.ok(contactService.getContactAndFriends(userDetails.getUsername()));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/add_contact")
    public ResponseEntity<ContactAnswer> updateContact(@RequestBody ContactAnswer request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            int userId = userService.getUserAsUser(userDetails.getUsername()).getUserId();

            if (request.user1() != userId || request.user2() == userId) return ResponseEntity.badRequest().build();

            return ResponseEntity.ok(contactService.updateContact(request));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete_contact")
    public ResponseEntity<Boolean> deleteContact(@RequestBody ContactAnswer request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            int userId = userService.getUserAsUser(userDetails.getUsername()).getUserId();

            if (request.user1() != userId || request.user2() == userId) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(contactService.deleteContact(request));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
