package ru.parovozik.backend.service;



import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.TaskAnswer;
import ru.parovozik.backend.dto.TaskRequest;
import ru.parovozik.backend.dto.UpdateTime;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.RepeatTaskRepository;
import ru.parovozik.backend.repostitory.TaskRepository;
import ru.parovozik.backend.repostitory.UserRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.plaf.nimbus.State;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RepeatTaskRepository repeatTaskRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, RepeatTaskRepository repeatTaskRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.repeatTaskRepository = repeatTaskRepository;
    }


    public boolean createTask(TaskRequest taskRequest) {
        Task task = new Task();
        User user = userRepository.findUserByUsername(taskRequest.username());
        if(taskRepository.findByTitleAndStartAndEndingAndUser(taskRequest.title(), taskRequest.starting(), taskRequest.ending(), user) !=null)
            throw new KeyAlreadyExistsException();
        task.setTitle(taskRequest.title());
        task.setBody(taskRequest.body());
        task.setPushTemplate(taskRequest.pushTemplate());
        task.setColor(taskRequest.color());
        task.setPrivacy(taskRequest.privacy());
        task.setStatus(taskRequest.status());
        task.setStart(taskRequest.starting());
        task.setEnd(taskRequest.ending());
        task.setEdited(false);
        if(user!=null) {
            task.setUserId(user);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public Task getTask(String title, String username, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findUserByUsername(username);
        return taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
    }

    public boolean deleteTask(String title, String username, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            taskRepository.delete(task);
            return true;
        }
        return false;
    }

    public boolean updateTitle(String title, String username, LocalDateTime start, LocalDateTime end, String newTitle) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setTitle(newTitle);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateBody(String title, String username, LocalDateTime start, LocalDateTime end, String newBody) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setBody(newBody);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateStart(String title, String username, LocalDateTime start, LocalDateTime end, LocalDateTime newStart) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setStart(newStart);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePushTemplate(String title, String username, LocalDateTime start, LocalDateTime end, PushTemplate pushTemplate) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setPushTemplate(pushTemplate);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateStatus(String title, String username, LocalDateTime start, LocalDateTime end, Status status) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setStatus(status);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateColor(String title, String username, LocalDateTime start, LocalDateTime end, Color color) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setColor(color);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePrivacy(String title, String username, LocalDateTime start, LocalDateTime end, Privacy privacy) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setPrivacy(privacy);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateEdited(String title, String username, LocalDateTime start, LocalDateTime end, boolean isEdited) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setEdited(isEdited);
            taskRepository.save(task);
            return true;
        }
        return false;
    }


    public boolean updateEnd(String title, String username, LocalDateTime start, LocalDateTime end, LocalDateTime newEnd) {
        User user = userRepository.findUserByUsername(username);
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title,start,end,user);
            task.setEnd(newEnd);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateTime(UpdateTime updateTime) {
        User user = userRepository.findUserByUsername(updateTime.username());
        if(user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(updateTime.title(),updateTime.start(),updateTime.end(),user);
            task.setStart(updateTime.newStart());
            task.setEnd(updateTime.newEnd());
            taskRepository.save(task);
            return true;
        }
        return false;
    }


    @Transactional
    public List<TaskAnswer> returnAllTasks(String username) {
        User user = userRepository.findUserByUsername(username);
        return returnTasks(user,List.of(Privacy.PRIVATE,Privacy.FRIENDS,Privacy.PUBLIC));
    }

    @Transactional
    public List<TaskAnswer> returnFriendTasks(String username) {
        User user = userRepository.findUserByUsername(username);
        return returnTasks(user,List.of(Privacy.FRIENDS,Privacy.PUBLIC));
    }

    @Transactional
    public List<TaskAnswer> returnOrdinalTasks(String username) {
        User user = userRepository.findUserByUsername(username);
        return returnTasks(user,List.of(Privacy.PUBLIC));
    }

    private List<TaskAnswer> returnTasks(User user, List<Privacy> pr) {
        List<TaskAnswer> ls = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minus = now.minusMonths(1);
        LocalDateTime plus   = now.plusMonths(1);
        if(user != null) {
            List<Task> tasks = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIsNull(user, minus,plus);
            for(Task task : tasks) {
                if(pr.contains(task.getPrivacy()))
                    ls.add(toTaskAnswer(task));
            }
            List<RepeatTask> repeatTasks = repeatTaskRepository.findRepeatTaskByUserAndGlobalEndAfter(user, minus);
            Map<LocalDateTime, Task> overrides = new HashMap<>();
            List<Task> allOverrides = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIn(
                    user, minus,plus, repeatTasks);
            allOverrides.forEach(t -> overrides.put(t.getStart(), t));
            for(RepeatTask rp : repeatTasks) {
                LocalDateTime time = rp.getStart();
                Period per = rp.getBeforeHowDays();
                Duration dur = rp.getBeforeHowHours();
                Duration diff = Duration.between(rp.getStart(), rp.getEnd());
                while(!time.isAfter(minus)) {
                    time = time.plus(per).plus(dur);
                }
                while(time.isBefore(plus) || time.isBefore(rp.getGlobalEnd())) {
                    Task taskFind = overrides.get(time);
                    if (taskFind != null && pr.contains(taskFind.getPrivacy()))
                        ls.add(toTaskAnswer(taskFind));
                    else
                        if(pr.contains(rp.getPrivacy()))
                            ls.add(toTaskAnswer(rp, time, time.plus(diff)));
                    time = time.plus(per).plus(dur);
                }
            }
        }
        return ls;
    }

    private List<TaskAnswer> returnTasksByMonth(User user, List<Privacy> pr,LocalDateTime start, LocalDateTime end) {
        List<TaskAnswer> ls = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minus = start;
        LocalDateTime plus   = end;
        if(user != null) {
            List<Task> tasks = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIsNull(user, minus,plus);
            for(Task task : tasks) {
                if(pr.contains(task.getPrivacy()))
                    ls.add(toTaskAnswer(task));
            }
            List<RepeatTask> repeatTasks = repeatTaskRepository.findRepeatTaskByUserAndGlobalEndAfter(user, minus);
            Map<LocalDateTime, Task> overrides = new HashMap<>();
            List<Task> allOverrides = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIn(
                    user, minus,plus, repeatTasks);
            allOverrides.forEach(t -> overrides.put(t.getStart(), t));
            for(RepeatTask rp : repeatTasks) {
                LocalDateTime time = rp.getStart();
                Period per = rp.getBeforeHowDays();
                Duration dur = rp.getBeforeHowHours();
                Duration diff = Duration.between(rp.getStart(), rp.getEnd());
                while(!time.isAfter(minus)) {
                    time = time.plus(per).plus(dur);
                }
                while(time.isBefore(plus) || time.isBefore(rp.getGlobalEnd())) {
                    Task taskFind = overrides.get(time);
                    if (taskFind != null && pr.contains(taskFind.getPrivacy()))
                        ls.add(toTaskAnswer(taskFind));
                    else
                    if(pr.contains(rp.getPrivacy()))
                        ls.add(toTaskAnswer(rp, time, time.plus(diff)));
                    time = time.plus(per).plus(dur);
                }
            }
        }
        return ls;
    }


    private TaskAnswer toTaskAnswer(Task task) {
        return new TaskAnswer(task.getTitle(), task.getBody(), task.getStatus(), task.getPrivacy(),
                task.getColor(), task.getStart(), task.getEnd(), task.getPushTemplate(),(task.getRepeatTask() != null ? task.getRepeatTask().getId() : null));
    }

    private TaskAnswer toTaskAnswer(RepeatTask rp, LocalDateTime start, LocalDateTime end) {
        return new TaskAnswer(rp.getTitle(), rp.getBody(), rp.getStatus(), rp.getPrivacy(),
                rp.getColor(), start, end, rp.getPushTemplate(), null);
    }

}
