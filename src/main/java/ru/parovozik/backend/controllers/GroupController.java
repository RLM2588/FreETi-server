package ru.parovozik.backend.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.GroupAnswer;
import ru.parovozik.backend.entity.Groups;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Role;
import ru.parovozik.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parovozik.backend.service.GroupService;
import ru.parovozik.backend.service.TaskService;
import ru.parovozik.backend.service.UserService;

import java.time.*;
        import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final AuthService authService;
    private final TaskService taskService;
    private final UserService userService;
    private final GroupService groupService;

    public GroupController(AuthService authService, TaskService taskService, UserService userService, GroupService groupService) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupAnswer>> getGroupsByLogin(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(groupService.getUserGroupsAsAnswer(userDetails.getUsername()));
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupAnswer> createGroup(@RequestBody GroupAnswer groupRequest, @AuthenticationPrincipal UserDetails userDetails) {
        UUID newId = groupService.createGroup(groupRequest.title(), groupRequest.body());
        groupService.addUser(userDetails.getUsername(), newId, Role.OWNER);
        return ResponseEntity.ok(new GroupAnswer(newId, groupRequest.title(), groupRequest.body(), false));
    }

    @PutMapping("/groups")
    public ResponseEntity<GroupAnswer> updateGroup(@RequestBody GroupAnswer groupRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Groups group = groupService.getGroupInfo(groupRequest.id());


    }

}
