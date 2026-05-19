package ru.parovozik.backend.controllers;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.Contact;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.repostitory.ContactRepository;
import ru.parovozik.backend.repostitory.TaskRepository;
import ru.parovozik.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parovozik.backend.service.ContactService;
import ru.parovozik.backend.service.TaskService;
import ru.parovozik.backend.service.UserService;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final AuthService authService;
    private final TaskService taskService;
    private final UserService userService;
    private final ContactService contactService;
    private final ContactRepository contactRepository;
    private final TaskRepository taskRepository;


    public TaskController(AuthService authService, TaskService taskService, UserService userService, ContactService contactService, ContactRepository contactRepository, TaskRepository taskRepository) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
        this.contactService = contactService;
        this.contactRepository = contactRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/othertasks")
    public ResponseEntity<List<OtherTaskAnswer>> getOtherUserTasks(@RequestParam("yearMonth") String yearMonth, @RequestParam("login") String login, @AuthenticationPrincipal UserDetails userDetails) {
        User userReq = userService.getUserAsUser(userDetails.getUsername());
        User secondUser = userService.getUserAsUser(login);
        Contact contact = contactRepository.findAllByFirstAndSecond(userReq, secondUser);
        if (contact == null) return ResponseEntity.badRequest().build();

        if (contact.isFriend())
            return ResponseEntity.ok(taskService.returnFriendTasks(login, yearMonth));

        return ResponseEntity.ok(taskService.returnOrdinalTasks(login, yearMonth));
    }

    @GetMapping("/username_id")
    public ResponseEntity<UsernameIdAnswer> getusernameId(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new UsernameIdAnswer(userDetails.getUsername(), userService.getUserAsUser(userDetails.getUsername()).getUserId()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskAnswer>> getTasks(@RequestParam(name = "yearMonth") String yearMonthEntry, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getUserAsUser(username);
        YearMonth yearMonth = YearMonth.parse(yearMonthEntry);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();

        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);


        //return ResponseEntity.ok(taskService.returnAllTasks(userDetails.getUsername()));

        return ResponseEntity.ok(taskService.returnTasksByMonth(user, List.of(Privacy.PRIVATE, Privacy.PUBLIC, Privacy.FRIENDS), startOfMonth, endOfMonth));
    }

    @GetMapping("/tasks/update")
    public ResponseEntity<List<TaskAnswer>> getUpdatedTasks(@RequestParam(name = "yearMonth") String yearMonthEntry,
                                                            @RequestParam(name = "since") Long since, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getUserAsUser(username);
        YearMonth yearMonth = YearMonth.parse(yearMonthEntry);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();

        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        //return ResponseEntity.ok(taskService.returnAllTasks(userDetails.getUsername()));
        return ResponseEntity.ok(taskService.returnTasksByMonth(user, List.of(Privacy.PRIVATE, Privacy.PUBLIC, Privacy.FRIENDS), startOfMonth, endOfMonth));
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<TaskAnswer>> getUnassigned(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.getUserAsUser(username);
        List<TaskAnswer> answers = taskService.returnTasksByMonth(user, List.of(Privacy.PRIVATE, Privacy.PUBLIC, Privacy.FRIENDS), toLocalDateTime(Instant.ofEpochMilli(0)), toLocalDateTime(Instant.ofEpochMilli(0)));

        return ResponseEntity.ok(answers);
    }

    @PatchMapping("tasks")
    public ResponseEntity<TaskAnswer> updateTask(@RequestBody TaskAnswer incomingTask, @AuthenticationPrincipal UserDetails userDetails) {
        Task updatedTask;
        updatedTask = taskService.updateTask(incomingTask, userDetails.getUsername());

        /*if (taskRepository.findById(incomingTask.id()).isPresent()) {
            updatedTask = taskService.updateTask(incomingTask, userDetails.getUsername());
        }
        else {
            boolean res = taskService.createTask(incomingTask, userDetails.getUsername());
            if (!res) return ResponseEntity.badRequest().build();
            Optional<Task> res2 = taskRepository.findById(incomingTask.id());
            return res2.map(task -> ResponseEntity.ok(Task.toTaskAnswer(task))).orElseGet(() -> ResponseEntity.badRequest().build());
        }*/
        return ResponseEntity.ok(taskService.toTaskAnswer(updatedTask));
    }

    @PostMapping("/tasks")
    public TaskAnswer addTask(@RequestBody TaskRequest newTask, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        //taskService.createTask(newTask, userDetails.getUsername());
        return taskService.getTask(newTask.title(), userDetails.getUsername(), toLocalDateTime(newTask.start()), toLocalDateTime(newTask.time_end()));
    }

    private LocalDateTime toLocalDateTime(Instant start) {
        return taskService.toLocalDateTime(start);

        //long epochSec = start.getEpochSecond();
        //return Instant.ofEpochMilli(epochSec).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Instant toInstant(LocalDateTime start) { //парсинг для ответа
        return start.toInstant(ZoneOffset.UTC);
    }

    /*@GetMapping("tasks/update")
    public List<TaskAnswer> getUpdatedTasks(@RequestParam(name = "yearMonth") String yearMonth,
                                      @RequestParam(name = "since") Long since) {

    }

    @PatchMapping("tasks")
    public boolean updateTask(@RequestBody TaskRequest updatedTask) {

    }



    @GetMapping("othertasks")
    public*/


    /*@PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokens = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out");
    }*/
}