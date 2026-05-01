package ru.parovozik.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.model.Role;
import ru.parovozik.backend.repostitory.*;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository;
    private final GroupUsersRepository groupUsersRepository;
    private final GroupEventsRepository groupEventsRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;


    public GroupService(GroupsRepository groupsRepository, UserRepository userRepository, GroupUsersRepository groupUsersRepository, GroupEventsRepository groupEventsRepository, PollRepository pollRepository, VoteRepository voteRepository) {
        this.groupsRepository = groupsRepository;
        this.userRepository = userRepository;
        this.groupUsersRepository = groupUsersRepository;
        this.groupEventsRepository = groupEventsRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    public List<GroupsUsers> getUserGroups(String username)  {
        User user = userRepository.findUserByUsername(username);
        return groupUsersRepository.findAllByUser(user);
    }

    public Groups getGroupInfo(UUID uuid)  {
        return groupsRepository.getById(uuid);
    }


    public boolean createGroup(String title, String body) {
        try {
            Groups group = new Groups();
            group.setTitle(title);
            group.setBody(body);
            groupsRepository.save(group);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public boolean deleteGroup(UUID uuid) {
        try {
            Groups group = groupsRepository.getById(uuid);
            groupUsersRepository.deleteAllByGroup(group);
            List<GroupEvents> groupEventsList= groupEventsRepository.findAllByGroup(group);
            List<Poll> pollList = pollRepository.findAllByGroupEventIn(groupEventsList);
            for(Poll one: pollList) {
                voteRepository.deleteAllByPoll(one);
            }
            for(GroupEvents groupEvents1 : groupEventsList) {
                pollRepository.deleteAllByGroupEvent(groupEvents1);
            }
            groupsRepository.delete(group);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean addUser(String username, UUID group, Role role) {
        try {
            User user = userRepository.findUserByUsername(username);
            Groups group1 = groupsRepository.getById(group);
            GroupsUsers groupsUsers = new GroupsUsers();
            groupsUsers.setUser(user);
            groupsUsers.setGroup(group1);
            groupsUsers.setRole(role);
            groupUsersRepository.save(groupsUsers);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Role getRole(String username, UUID group) {
        try {
            User user = userRepository.findUserByUsername(username);
            Groups group1 = groupsRepository.getById(group);
            return groupUsersRepository.findByUserAndGroup(user,group1).getRole();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateRole(String username, UUID group, Role role) {
        try {
            User user = userRepository.findUserByUsername(username);
            Groups group1 = groupsRepository.getById(group);
            GroupsUsers groupsUsers = groupUsersRepository.findByUserAndGroup(user,group1);
            groupsUsers.setRole(role);
            groupUsersRepository.save(groupsUsers);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean deleteUser(String username, UUID group) {
        try {
            User user = userRepository.findUserByUsername(username);
            Groups group1 = groupsRepository.getById(group);
            GroupsUsers groupsUsers = groupUsersRepository.findByUserAndGroup(user,group1);
            groupUsersRepository.delete(groupsUsers);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateTitle(UUID uuid, String newTitle) {
        try {
            Groups group = groupsRepository.getById(uuid);
            group.setTitle(newTitle);
            groupsRepository.save(group);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateBody(UUID uuid, String newBody) {
        try {
            Groups group = groupsRepository.getById(uuid);
            group.setBody(newBody);
            groupsRepository.save(group);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


}
