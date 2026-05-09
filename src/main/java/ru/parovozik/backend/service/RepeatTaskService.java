package ru.parovozik.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.RepeatTaskRequest;
import ru.parovozik.backend.dto.TaskRequest;
import ru.parovozik.backend.dto.UpdateTime;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.RepeatTaskRepository;
import ru.parovozik.backend.repostitory.TaskRepository;
import ru.parovozik.backend.repostitory.UserRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class RepeatTaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RepeatTaskRepository repeatTaskRepository;
    private final TaskService taskService;

    public RepeatTaskService(UserRepository userRepository, TaskRepository taskRepository, RepeatTaskRepository repeatTaskRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.repeatTaskRepository = repeatTaskRepository;
        this.taskService = taskService;
    }

    public boolean createTask(RepeatTaskRequest repeatTaskRequest) {
        RepeatTask task = new RepeatTask();
        try {
            task.setTitle(repeatTaskRequest.title());
        }
        catch (Exception e) {
            throw new KeyAlreadyExistsException("Such title is already in use");
        }
        task.setBody(repeatTaskRequest.body());
        task.setPushTemplate(repeatTaskRequest.pushTemplate());
        task.setColor(repeatTaskRequest.color());
        task.setPrivacy(repeatTaskRequest.privacy());
        task.setStatus(repeatTaskRequest.status());
        task.setStart(repeatTaskRequest.starting());
        task.setBeforeHowDays(repeatTaskRequest.period());
        task.setBeforeHowHours(repeatTaskRequest.duration());
        User user = userRepository.findUserByUsername(repeatTaskRequest.username());
        if(user!=null) {
            task.setUserId(user);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean deleteTask(String title, String username) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            List<Task> allOverrides = taskRepository.findAllByUserAndRepeatTask(
                    user, task);
            taskRepository.deleteAll(allOverrides);
            repeatTaskRepository.delete(task);
            return true;
        }
        return false;
    }

    public RepeatTask getRepeatTask(String title, String username) {
        User user = userRepository.findUserByUsername(username);
        return repeatTaskRepository.findByTitleAndUser(title,user);
    }

    public boolean updateTitle(String title, String username, String newTitle) {
        User user = userRepository.findUserByUsername(username);
        try {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setTitle(newTitle);
            repeatTaskRepository.save(task);
            return true;
        }
        catch (Exception e) {
            throw new KeyAlreadyExistsException("Such title is already in use");
        }
    }

    public boolean updateBody(String title, String username, String newBody) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setBody(newBody);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateStatus(String title, String username, Status status) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setStatus(status);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateColor(String title, String username, String color) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setColor(color);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePrivacy(String title, String username,  Privacy privacy) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setPrivacy(privacy);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateEdited(String title, String username, boolean isEdited) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setEdited(isEdited);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateTime(UpdateTime updateTime) {
        User user = userRepository.findUserByUsername(updateTime.username());
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(updateTime.title(),user);
            task.setStart(updateTime.newStart());
            task.setEnd(updateTime.newEnd());
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePeriod(String title, String username, Period period) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setBeforeHowDays(period);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateDuration(String title, String username, Duration duration) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setBeforeHowHours(duration);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateInterval(String title, String username, Period period, Duration duration) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            RepeatTask task = repeatTaskRepository.findByTitleAndUser(title,user);
            task.setBeforeHowDays(period);
            task.setBeforeHowHours(duration);
            repeatTaskRepository.save(task);
            return true;
        }
        return false;
    }
}
