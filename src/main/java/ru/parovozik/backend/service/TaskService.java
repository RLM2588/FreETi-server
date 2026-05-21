package ru.parovozik.backend.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.OtherTaskAnswer;
import ru.parovozik.backend.dto.TaskAnswer;
import ru.parovozik.backend.dto.UpdateTime;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.PushTemplateRepository;
import ru.parovozik.backend.repostitory.RepeatTaskRepository;
import ru.parovozik.backend.repostitory.TaskRepository;
import ru.parovozik.backend.repostitory.UserRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.*;
import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RepeatTaskRepository repeatTaskRepository;
    private final PushTemplateRepository pushTemplateRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, RepeatTaskRepository repeatTaskRepository, PushTemplateRepository pushTemplateRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.repeatTaskRepository = repeatTaskRepository;
        this.pushTemplateRepository = pushTemplateRepository;
    }

    public List<TaskAnswer> returnTasksForUsersInPeriod(List<Integer> userIds,
                                                        List<Privacy> privacyList,
                                                        Instant globalStart,
                                                        Instant globalEnd,
                                                        int importance) {
        return taskRepository.findTasksForUsersInPeriod(userIds, privacyList, toLocalDateTime(globalStart), toLocalDateTime(globalEnd),importance).stream().map(Task::toTaskAnswer).toList();
    }

    public boolean createTask(TaskAnswer taskRequest, String username) {
        Task task = new Task();
        System.out.println(username);
        User user = userRepository.findUserByUsername(username);
        System.out.println(user.getUsername());
        if (taskRepository.findById(taskRequest.id()).isPresent())
            throw new KeyAlreadyExistsException();
        //task.setId(taskRequest.id());
        task.setTitle(taskRequest.title());
        task.setBody(taskRequest.body());
        task.setPushTemplate(pushTemplateRepository.findPushTemplateByPushId(taskRequest.pushTemplate()));
        task.setColor(taskRequest.colour());
        task.setPrivacy(taskRequest.privacy());
        task.setStatus(taskRequest.status());
        task.setStart(toLocalDateTime(taskRequest.start()));
        task.setEnd(toLocalDateTime(taskRequest.time_end()));
        task.setEdited(false);
        if (user != null) {
            System.out.println("saved");
            task.setUserId(user);
            task.setClientUuid(taskRequest.id());
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public TaskAnswer getTask(String title, String username, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findUserByUsername(username);
        Iterable<User> us = userRepository.findAll();
        return this.toTaskAnswer(taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user));
    }

    public boolean deleteTask(String title, String username, LocalDateTime start, LocalDateTime end) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            taskRepository.delete(task);
            return true;
        }
        return false;
    }

    public boolean updateTitle(String title, String username, LocalDateTime start, LocalDateTime end, String newTitle) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setTitle(newTitle);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    private void updateFields(Task task, TaskAnswer incomingTask) {
        task.setTitle(incomingTask.title());
        task.setBody(incomingTask.body());

        task.setStart(toLocalDateTime(incomingTask.start()));
        task.setEnding(toLocalDateTime(incomingTask.time_end()));

        task.setStatus(incomingTask.status());
        task.setPrivacy(incomingTask.privacy());
        task.setImportance(incomingTask.importance());
        task.setColor(incomingTask.colour());

        if (incomingTask.updated_at() != null) {
            task.setCreatedAt(toLocalDateTime(incomingTask.updated_at()));
        }

        task.setEdited(true);
    }

    public boolean updateBody(String title, String username, LocalDateTime start, LocalDateTime end, String newBody) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setBody(newBody);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateStart(String title, String username, LocalDateTime start, LocalDateTime end, LocalDateTime newStart) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setStart(newStart);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePushTemplate(String title, String username, LocalDateTime start, LocalDateTime end, PushTemplate pushTemplate) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setPushTemplate(pushTemplate);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateStatus(String title, String username, LocalDateTime start, LocalDateTime end, Status status) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setStatus(status);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateColor(String title, String username, LocalDateTime start, LocalDateTime end, String color) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setColor(color);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updatePrivacy(String title, String username, LocalDateTime start, LocalDateTime end, Privacy privacy) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setPrivacy(privacy);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateEdited(String title, String username, LocalDateTime start, LocalDateTime end, boolean isEdited) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setEdited(isEdited);
            taskRepository.save(task);
            return true;
        }
        return false;
    }


    public boolean updateEnd(String title, String username, LocalDateTime start, LocalDateTime end, LocalDateTime newEnd) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(title, start, end, user);
            task.setEnd(newEnd);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean updateTime(UpdateTime updateTime) {
        User user = userRepository.findUserByUsername(updateTime.username());
        if (user != null) {
            Task task = taskRepository.findByTitleAndStartAndEndingAndUser(updateTime.title(), updateTime.start(), updateTime.end(), user);
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
        System.out.println(user.getUserId());
        return returnTasks(user, List.of(Privacy.PRIVATE, Privacy.FRIENDS, Privacy.PUBLIC));
    }

    @Transactional
    public List<OtherTaskAnswer> returnFriendTasks(String username, String yearMonthEntry) {
        User user = userRepository.findUserByUsername(username);
        int lastIndex = yearMonthEntry.lastIndexOf('-');
        String ym = yearMonthEntry.substring(0, lastIndex);
        int day = Integer.parseInt(yearMonthEntry.substring(lastIndex + 1));

        LocalDateTime startOfMonth;
        LocalDateTime endOfMonth;
//        System.out.println(yearMonthEntry + " " + ym + " " + String.valueOf(day));
        YearMonth yearMonth = YearMonth.parse(ym);
        if (day == 1) {
            endOfMonth = yearMonth.atDay(2).atTime(LocalTime.MAX);
            yearMonth.minusMonths(1);
            startOfMonth = yearMonth.atEndOfMonth().atStartOfDay();
        } else if (!yearMonth.isValidDay(day + 1)) {
            startOfMonth = yearMonth.atDay(day - 1).atStartOfDay();
            yearMonth.plusMonths(1);
            endOfMonth = yearMonth.atDay(1).atTime(LocalTime.MAX);
        } else {
            startOfMonth = yearMonth.atDay(1).atStartOfDay();
            endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        }
        return returnOtherTasksByMonth(user, List.of(Privacy.FRIENDS, Privacy.PUBLIC), startOfMonth, endOfMonth);
    }

    @Transactional
    public List<OtherTaskAnswer> returnOrdinalTasks(String username, String yearMonthEntry) {
        User user = userRepository.findUserByUsername(username);
        int lastIndex = yearMonthEntry.lastIndexOf('-');
        String ym = yearMonthEntry.substring(0, lastIndex);
        int day = Integer.parseInt(yearMonthEntry.substring(lastIndex + 1));

        System.out.println(yearMonthEntry + " " + ym + " " + String.valueOf(day));

        LocalDateTime startOfMonth;
        LocalDateTime endOfMonth;

        YearMonth yearMonth = YearMonth.parse(ym);
        if (day == 1) {
            endOfMonth = yearMonth.atDay(2).atTime(LocalTime.MAX);
            yearMonth.minusMonths(1);
            startOfMonth = yearMonth.atEndOfMonth().atStartOfDay();
        } else if (!yearMonth.isValidDay(day + 1)) {
            startOfMonth = yearMonth.atDay(day - 1).atStartOfDay();
            yearMonth.plusMonths(1);
            endOfMonth = yearMonth.atDay(1).atTime(LocalTime.MAX);
        } else {
            startOfMonth = yearMonth.atDay(1).atStartOfDay();
            endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        }
        return returnOtherTasksByMonth(user, List.of(Privacy.PUBLIC), startOfMonth, endOfMonth);
    }

    private List<TaskAnswer> returnTasks(User user, List<Privacy> pr) {
        List<TaskAnswer> ls = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minus = now.minusMonths(1);
        LocalDateTime plus = now.plusMonths(1);
        if (user != null) {
            List<Task> tasks = taskRepository.findByUserAndStartBetweenAndRepeatTaskIsNull(user, minus, plus);
            for (Task task : tasks) {
                if (pr.contains(task.getPrivacy()))
                    ls.add(toTaskAnswer(task));
            }
            List<RepeatTask> repeatTasks = repeatTaskRepository.findRepeatTaskByUserAndGlobalEndAfter(user, minus);
            Map<LocalDateTime, Task> overrides = new HashMap<>();
            List<Task> allOverrides = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIn(
                    user, minus, plus, repeatTasks);
            allOverrides.forEach(t -> overrides.put(t.getStart(), t));
            for (RepeatTask rp : repeatTasks) {
                LocalDateTime time = rp.getStart();
                Period per = rp.getBeforeHowDays();
                Duration dur = rp.getBeforeHowHours();
                Duration diff = Duration.between(rp.getStart(), rp.getEnd());
                while (!time.isAfter(minus)) {
                    time = time.plus(per).plus(dur);
                }
                while (time.isBefore(plus) || time.isBefore(rp.getGlobalEnd())) {
                    Task taskFind = overrides.get(time);
                    if (taskFind != null && pr.contains(taskFind.getPrivacy()))
                        ls.add(toTaskAnswer(taskFind));
                    else if (pr.contains(rp.getPrivacy()))
                        ls.add(toTaskAnswer(rp, time, time.plus(diff)));
                    time = time.plus(per).plus(dur);
                }
            }
        }
        return ls;
    }

    public List<TaskAnswer> returnTasksByMonth(User user, List<Privacy> pr, LocalDateTime start, LocalDateTime end) {
        List<TaskAnswer> ls = new ArrayList<>();

        if (user != null) {
            List<Task> tasks = taskRepository.findByUserAndStartBetweenAndRepeatTaskIsNull(user, start, end);
            for (Task task : tasks) {
                if (pr.contains(task.getPrivacy())) {
                    ls.add(toTaskAnswer(task));
                    System.out.println(task.getStart());
                }
            }
            List<RepeatTask> repeatTasks = repeatTaskRepository.findRepeatTaskByUserAndGlobalEndAfter(user, start);
            Map<LocalDateTime, Task> overrides = new HashMap<>();
            List<Task> allOverrides = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIn(
                    user, start, end, repeatTasks);
            allOverrides.forEach(t -> overrides.put(t.getStart(), t));
            for (RepeatTask rp : repeatTasks) {
                LocalDateTime time = rp.getStart();
                Period per = rp.getBeforeHowDays();
                Duration dur = rp.getBeforeHowHours();
                Duration diff = Duration.between(rp.getStart(), rp.getEnd());
                while (!time.isAfter(start)) {
                    time = time.plus(per).plus(dur);
                }
                while (time.isBefore(end) || time.isBefore(rp.getGlobalEnd())) {
                    Task taskFind = overrides.get(time);
                    if (taskFind != null && pr.contains(taskFind.getPrivacy()))
                        ls.add(toTaskAnswer(taskFind));
                    else if (pr.contains(rp.getPrivacy()))
                        ls.add(toTaskAnswer(rp, time, time.plus(diff)));
                    time = time.plus(per).plus(dur);
                }
            }
        }
        return ls;
    }

    public List<OtherTaskAnswer> returnOtherTasksByMonth(User user, List<Privacy> pr, LocalDateTime start, LocalDateTime end) {
        List<OtherTaskAnswer> ls = new ArrayList<>();
        if (user != null) {
            List<Task> tasks = taskRepository.findByUserAndStartBetweenAndRepeatTaskIsNull(user, start, end);
            for (Task task : tasks) {
                if (pr.contains(task.getPrivacy()))
                    ls.add(toOtherTaskAnswer(task));
            }
            List<RepeatTask> repeatTasks = repeatTaskRepository.findRepeatTaskByUserAndGlobalEndAfter(user, start);
            Map<LocalDateTime, Task> overrides = new HashMap<>();
            List<Task> allOverrides = taskRepository.findByUserAndEndingBetweenAndRepeatTaskIn(
                    user, start, end, repeatTasks);
            allOverrides.forEach(t -> overrides.put(t.getStart(), t));
            /*for(RepeatTask rp : repeatTasks) {
                LocalDateTime time = rp.getStart();
                Period per = rp.getBeforeHowDays();
                Duration dur = rp.getBeforeHowHours();
                Duration diff = Duration.between(rp.getStart(), rp.getEnd());
                while(!time.isAfter(start)) {
                    time = time.plus(per).plus(dur);
                }
                while(time.isBefore(time_end) || time.isBefore(rp.getGlobalEnd())) {
                    Task taskFind = overrides.get(time);
                    if (taskFind != null && pr.contains(taskFind.getPrivacy()))
                        ls.add(toOtherTaskAnswer(taskFind));
                    else
                    if(pr.contains(rp.getPrivacy()))
                        ls.add(toTaskAnswer(rp, time, time.plus(diff)));
                    time = time.plus(per).plus(dur);
                }
            }*/
            // #TODO потом будут повторяющиеся задачи
        }
        return ls;
    }


    public TaskAnswer toTaskAnswer(Task task) {
        return new TaskAnswer(task.getClientUuid(), task.getTitle(), task.getBody(), task.getStatus(), task.getPrivacy(),
                task.getColor(), toInstant(task.getStart()), toInstant(task.getEnd()), 1, task.getImportance(), toInstant(task.getCreatedAt()));
    }

    public OtherTaskAnswer toOtherTaskAnswer(Task task) {
        return new OtherTaskAnswer(task.getClientUuid(), task.getTitle(), task.getBody(), task.getUserId().getUserId(), task.getStatus(), task.getPrivacy(), task.getColor(), toInstant(task.getStart()), toInstant(task.getEnd()), 1, task.getImportance());
    }

    // Нужно будет разобраться что здесь делать вместо null
    private TaskAnswer toTaskAnswer(RepeatTask rp, LocalDateTime start, LocalDateTime end) {
        return new TaskAnswer(null, rp.getTitle(), rp.getBody(), rp.getStatus(), rp.getPrivacy(),
                rp.getColor(), toInstant(start), toInstant(end), 1, rp.getImportance(), toInstant(rp.getCreatedAt()));
    }

    public LocalDateTime toLocalDateTime(Instant start) {
        long epochSec = start.getEpochSecond();
        return Instant.ofEpochMilli(epochSec).atZone(ZoneId.systemDefault()).toLocalDateTime();
        //return LocalDateTime.ofInstant(start, ZoneId.systemDefault());
    }

    public Instant toInstant(LocalDateTime start) {
        return start.toInstant(ZoneOffset.UTC);
    }


    @Transactional
    public Task updateTask(TaskAnswer incomingTask, String username) {
        return taskRepository.findByClientUuidAndUser(incomingTask.id(), userRepository.findUserByUsername(username))
                .map(existingTask -> {
                    System.out.println("aaaaaaaaaaaaaaaaaaaa");
                    if (incomingTask.updated_at().isAfter(toInstant(existingTask.getCreatedAt()))) {
                        updateFields(existingTask, incomingTask);
                        return taskRepository.save(existingTask);
                    }
                    return existingTask;
                })
                .orElseGet(() -> {
                    Task newTask = new Task();
                    newTask.setClientUuid(incomingTask.id());
                    newTask.setUser(userRepository.findUserByUsername(username));

                    updateFields(newTask, incomingTask);
                    Task t = taskRepository.save(newTask);
                    newTask = t;


                    Optional<Task> tt = taskRepository.findByClientUuidAndUser(newTask.getClientUuid(), newTask.getUser());
                    if (tt.isPresent()) return tt.get();
                    try {
                        throw new Exception("aaaaaaaaaaa");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
