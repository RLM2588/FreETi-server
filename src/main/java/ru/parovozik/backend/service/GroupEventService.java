package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.GroupTaskRequest;
import ru.parovozik.backend.dto.TaskRequest;
import ru.parovozik.backend.dto.UpdateTime;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.GroupEventsRepository;
import ru.parovozik.backend.repostitory.GroupsRepository;
import ru.parovozik.backend.repostitory.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GroupEventService {
    private final GroupEventsRepository groupEventsRepository;
    private final UserRepository userRepository;
    private final GroupsRepository groupsRepository;

    public GroupEventService(GroupEventsRepository groupEventsRepository, UserRepository userRepository, GroupsRepository groupsRepository) {
        this.groupEventsRepository = groupEventsRepository;
        this.userRepository = userRepository;
        this.groupsRepository = groupsRepository;
    }

    public UUID createGroupTask(GroupTaskRequest taskRequest) {
        try {
        GroupEvents task = new GroupEvents();
        User user = userRepository.findUserByUsername(taskRequest.username());
        task.setTitle(taskRequest.title());
        task.setBody(taskRequest.body());
        task.setPushTemplate(taskRequest.pushTemplate());
        task.setColor(taskRequest.color());
        task.setPrivacy(taskRequest.privacy());
        task.setStatus(taskRequest.status());
        task.setGroup(groupsRepository.getById(taskRequest.group()));
            if(user!=null) {
                task.setCreateByUser(user);
                groupEventsRepository.save(task);
                return task.getId();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return null;
    }

    public boolean setUpGroupTask(UUID uuid, LocalDateTime start, LocalDateTime end) {
        try {
            GroupEvents groupEvents = groupEventsRepository.findById(uuid).get();
            groupEvents.setStart(start);
            groupEvents.setEnd(end);
            groupEventsRepository.save(groupEvents);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean deleteGroupTask(UUID uuid) {
        try {
            GroupEvents groupEvents = groupEventsRepository.findById(uuid).get();
            groupEventsRepository.delete(groupEvents);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GroupEvents getGroupEvents(UUID uuid) {
        return groupEventsRepository.findById(uuid).get();
    }

    public boolean updateTitle(UUID uuid, String newTitle) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setTitle(newTitle);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateBody(UUID uuid, String newBody) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setBody(newBody);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateStart(UUID uuid, LocalDateTime start) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setStart(start);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateEnd(UUID uuid, LocalDateTime end) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setEnd(end);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updatePushTemplate(UUID uuid, PushTemplate pushTemplate) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setPushTemplate(pushTemplate);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateStatus(UUID uuid, Status status) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setStatus(status);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateColor(UUID uuid, Color color) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setColor(color);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updatePrivacy(UUID uuid, Privacy privacy) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setPrivacy(privacy);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean updateEdited(UUID uuid, boolean isEdited) {
        try {
            GroupEvents task = groupEventsRepository.findById(uuid).get();
            task.setEdited(isEdited);
            groupEventsRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }



}
