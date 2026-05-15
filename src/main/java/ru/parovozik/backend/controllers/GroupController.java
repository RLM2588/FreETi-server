package ru.parovozik.backend.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Role;
import ru.parovozik.backend.repostitory.GroupUsersRepository;
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
    private final GroupUsersRepository groupUsersRepository;

    public GroupController(AuthService authService, TaskService taskService, UserService userService, GroupService groupService, GroupUsersRepository groupUsersRepository) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
        this.groupService = groupService;
        this.groupUsersRepository = groupUsersRepository;
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
        User user = userService.getUserAsUser(userDetails.getUsername());
        GroupsUsers groupsUsers = groupUsersRepository.findByUserAndGroup(user, group);
        if (groupsUsers.getRole() == Role.OWNER) {
            groupService.updateBody(group.getId(), groupRequest.body());
            groupService.updateTitle(group.getId(), groupRequest.title());
        }
        return ResponseEntity.ok(groupService.getGroupInfo(groupRequest.id()).toGroupAnswer());
    }

    @PutMapping("/member_switch")
    public ResponseEntity<GroupUserAnswer> memberSwitch(@Param("user_id") int userId, @Param("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER && requestRole != Role.ADMIN) return (ResponseEntity<GroupUserAnswer>) ResponseEntity.badRequest();

        String userLogin = userService.getUserById(userId).username();
        Role userRole = groupService.getRole(userLogin, UUID.fromString(groupId));
        if (userRole == Role.OWNER) return (ResponseEntity<GroupUserAnswer>) ResponseEntity.badRequest();

        boolean res = groupService.updateRole(userLogin, UUID.fromString(groupId), userRole == Role.MEMBER ? Role.ADMIN : Role.MEMBER);

        if (!res) return (ResponseEntity<GroupUserAnswer>) ResponseEntity.badRequest();
        return ResponseEntity.ok(new GroupUserAnswer(groupId, userId, userRole == Role.MEMBER ? Role.ADMIN : Role.MEMBER));
    }

    @PutMapping("/member_add")
    public ResponseEntity<Boolean> memberAdd(@Param("user_id") int userId, @Param("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER && requestRole != Role.ADMIN) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        String userLogin = userService.getUserById(userId).username();
        boolean res = groupService.addUser(userLogin, UUID.fromString(groupId), Role.MEMBER);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/member_delete")
    public ResponseEntity<Boolean> memberDelete(@Param("user_id") int userId, @Param("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER && requestRole != Role.ADMIN) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();


        String userLogin = userService.getUserById(userId).username();
        Role userRole = groupService.getRole(userLogin, UUID.fromString(groupId));
        if (userRole == Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteUser(userLogin, UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }

    @PutMapping("/leave")
    public ResponseEntity<Boolean> leaveFromGroup(@Param("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole == Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteUser(userDetails.getUsername(), UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete_group")
    public ResponseEntity<Boolean> deleteGroup(@Param("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteGroup(UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }








}
