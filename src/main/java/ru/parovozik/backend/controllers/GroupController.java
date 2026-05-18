package ru.parovozik.backend.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.model.Role;
import ru.parovozik.backend.repostitory.GroupUsersRepository;
import ru.parovozik.backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final AuthService authService;
    private final TaskService taskService;
    private final UserService userService;
    private final GroupService groupService;
    private final GroupUsersRepository groupUsersRepository;
    private final GroupEventService groupEventService;


    public GroupController(AuthService authService, TaskService taskService, UserService userService, GroupService groupService, GroupUsersRepository groupUsersRepository, GroupEventService groupEventService) {
        this.authService = authService;
        this.taskService = taskService;
        this.userService = userService;
        this.groupService = groupService;
        this.groupUsersRepository = groupUsersRepository;
        this.groupEventService = groupEventService;
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
    public ResponseEntity<GroupUserAnswer> memberSwitch(@RequestParam("user_id") int userId, @RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
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
    public ResponseEntity<Boolean> memberAdd(@RequestParam("user_id") int userId, @RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER && requestRole != Role.ADMIN) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        String userLogin = userService.getUserById(userId).username();
        boolean res = groupService.addUser(userLogin, UUID.fromString(groupId), Role.MEMBER);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/member_delete")
    public ResponseEntity<Boolean> memberDelete(@RequestParam("user_id") int userId, @RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER && requestRole != Role.ADMIN) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();


        String userLogin = userService.getUserById(userId).username();
        Role userRole = groupService.getRole(userLogin, UUID.fromString(groupId));
        if (userRole == Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteUser(userLogin, UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/group_members")
    public ResponseEntity<List<GroupUserAnswer>> getMembers(@RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UUID groupUUID = UUID.fromString(groupId);
            groupService.getRole(userDetails.getUsername(), groupUUID);

            return ResponseEntity.ok(groupService.getUsersByGroup(groupUUID).stream().map(GroupsUsers::toGroupUserAnswer).toList());
        }
        catch (Exception e) {
            return (ResponseEntity<List<GroupUserAnswer>>) ResponseEntity.badRequest();
        }
    }

    @GetMapping("/group_users")
    public ResponseEntity<List<UserAnswer>> getMembersAsUsers(@RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UUID groupUUID = UUID.fromString(groupId);
            groupService.getRole(userDetails.getUsername(), groupUUID);

            return ResponseEntity.ok(groupService.getUsersByGroup(groupUUID).stream().map(groupsUsers -> groupsUsers.getUser().asUserAnswer()).toList());
        }
        catch (Exception e) {
            return (ResponseEntity<List<UserAnswer>>) ResponseEntity.badRequest();
        }
    }

    @PutMapping("/leave")
    public ResponseEntity<Boolean> leaveFromGroup(@RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole == Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteUser(userDetails.getUsername(), UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete_group")
    public ResponseEntity<Boolean> deleteGroup(@RequestParam("group_id") String groupId, @AuthenticationPrincipal UserDetails userDetails) {
        Role requestRole = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (requestRole != Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupService.deleteGroup(UUID.fromString(groupId));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/new_event")
    public ResponseEntity<List<GroupTaskAnswer>> createNewGroupEvent(@RequestParam("group_id") String groupId, @RequestBody GroupEventRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserAsUser(userDetails.getUsername());
        System.out.println(request.time_pick());
        System.out.println(request.importance());
        Role role = groupService.getRole(userDetails.getUsername(), UUID.fromString(groupId));
        if (role != Role.OWNER && role != Role.ADMIN) return new ResponseEntity<List<GroupTaskAnswer>>(HttpStatusCode.valueOf(403));


        List<GroupEventService.TimeInterval> intervals = groupEventService.findEventPeriods(groupId, user.getUserId(), request);

        return ResponseEntity.ok(groupEventService.mapIntervalsToGroupTasks(intervals, request.time_pick(), groupId, user, request));
    }


    @DeleteMapping("/delete_event")
    public ResponseEntity<Boolean> deleteEventTask(@RequestParam("event_id") String eventTaskUuid, @AuthenticationPrincipal UserDetails userDetails) {
        Groups group = groupEventService.getGroupEvents(UUID.fromString(eventTaskUuid)).getGroup();

        Role role = groupService.getRole(userDetails.getUsername(), group.getId());

        if (role != Role.ADMIN && role != Role.OWNER) return (ResponseEntity<Boolean>) ResponseEntity.badRequest();

        boolean res = groupEventService.deleteGroupTask(UUID.fromString(eventTaskUuid));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/group_tasks")
    public ResponseEntity<List<GroupTaskAnswer>> getGroupTasks(@RequestParam("yearMonth") String yearMonthDay, @RequestParam("id") String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserAsUser(userDetails.getUsername());
        UUID uuid = UUID.fromString(id);

        Role role = groupService.getRole(userDetails.getUsername(), uuid);
        //if (role != null) return (ResponseEntity<List<GroupTaskAnswer>>) ResponseEntity.badRequest(null);

        List<GroupTaskAnswer> tasks = groupEventService.getTasksByStartAndEnd(uuid, yearMonthDay);

        return ResponseEntity.ok(tasks);
    }
}
