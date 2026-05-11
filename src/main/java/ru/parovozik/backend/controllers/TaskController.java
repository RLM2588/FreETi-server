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
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final AuthService authService;
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(AuthService authService, TaskService taskService, UserService userService) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/username_id")
    public ResponseEntity<UsernameIdAnswer> getusernameId(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(new UsernameIdAnswer(userDetails.getUsername(), userService.getUserAsUser(userDetails.getUsername()).getUserId()));
    }

    @GetMapping("tasks")
    public ResponseEntity<List<TaskAnswer>> getTasks(@RequestParam(name = "yearMonth") String yearMonthEntry, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("aaaaa");
        String username = userDetails.getUsername();
        User user = userService.getUserAsUser(username);
        YearMonth yearMonth = YearMonth.parse(yearMonthEntry);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();

        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return ResponseEntity.ok(taskService.returnTasksByMonth(user, List.of(Privacy.PRIVATE, Privacy.PUBLIC, Privacy.FRIENDS), startOfMonth ,endOfMonth));
    }

    @GetMapping("tasks/update")
    public ResponseEntity<List<TaskAnswer>> getUpdatedTasks(@RequestParam(name = "yearMonth") String yearMonthEntry,
                                            @RequestParam(name = "since") Long since, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("aaaaa");
        String username = userDetails.getUsername();
        User user = userService.getUserAsUser(username);
        YearMonth yearMonth = YearMonth.parse(yearMonthEntry);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();

        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return ResponseEntity.ok(taskService.returnTasksByMonth(user, List.of(Privacy.PRIVATE, Privacy.PUBLIC, Privacy.FRIENDS), startOfMonth ,endOfMonth));
    }

    @PatchMapping("tasks")
    public ResponseEntity<TaskAnswer> updateTask(@RequestBody TaskAnswer incomingTask, @AuthenticationPrincipal UserDetails userDetails) {
        Task updatedTask = taskService.updateTask(incomingTask, userDetails.getUsername());

        return ResponseEntity.ok(taskService.toTaskAnswer(updatedTask));
    }

    @PostMapping("tasks")
    public TaskAnswer addTask(@RequestBody TaskRequest newTask, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(taskService.createTask(newTask,userDetails.getUsername()));
        return taskService.getTask(newTask.title(),userDetails.getUsername(), toLocalDateTime(newTask.start()),  toLocalDateTime(newTask.time_end()));
    }

    private LocalDateTime toLocalDateTime(Instant start) {;//парсинг для бд
        long epochSec = start.getEpochSecond();
        return Instant.ofEpochMilli(epochSec).atZone(ZoneId.systemDefault()).toLocalDateTime();
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