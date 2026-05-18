package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;
import ru.parovozik.backend.repostitory.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupEventService {
    private final GroupEventsRepository groupEventsRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final GroupsRepository groupsRepository;
    private final GroupUsersRepository groupUsersRepository;
    private final PushTemplateRepository pushTemplateRepository;

    public GroupEventService(GroupEventsRepository groupEventsRepository, UserRepository userRepository, TaskRepository taskRepository, TaskService taskService, GroupsRepository groupsRepository, GroupUsersRepository groupUsersRepository, PushTemplateRepository pushTemplateRepository) {
        this.groupEventsRepository = groupEventsRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.groupsRepository = groupsRepository;
        this.groupUsersRepository = groupUsersRepository;
        this.pushTemplateRepository = pushTemplateRepository;
    }

    public UUID createGroupTask(GroupTaskRequest taskRequest) {
        try {
            GroupEvents task = new GroupEvents();
            User user = userRepository.findUserByUsername(taskRequest.username());
            task.setTitle(taskRequest.title());
            task.setBody(taskRequest.body());
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

    public List<GroupTaskAnswer> getTasksByStartAndEnd(UUID uuid, String yearMonthEntry) {
        Groups group = groupsRepository.getById(uuid);

        int lastIndex = yearMonthEntry.lastIndexOf('-');
        String ym = yearMonthEntry.substring(0, lastIndex);
        int day = Integer.parseInt(yearMonthEntry.substring(lastIndex + 1));

        LocalDateTime startOfMonth;
        LocalDateTime endOfMonth;

        YearMonth yearMonth = YearMonth.parse(ym);
        if (day == 1) {
            endOfMonth = yearMonth.atDay(2).atTime(LocalTime.MAX);
            yearMonth.minusMonths(1);
            startOfMonth = yearMonth.atEndOfMonth().atStartOfDay();
        }
        else if (!yearMonth.isValidDay(day + 1)) {
            startOfMonth = yearMonth.atDay(day - 1).atStartOfDay();
            yearMonth.plusMonths(1);
            endOfMonth = yearMonth.atDay(1).atTime(LocalTime.MAX);
        }
        else {
            startOfMonth = yearMonth.atDay(1).atStartOfDay();
            endOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        }

        List<GroupEvents> events = groupEventsRepository.findAllByGroupAndEndingBetween(group, startOfMonth, endOfMonth);
        return events.stream().map(GroupEvents::toGroupTaskAnswer).toList();
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

    public boolean updateColor(UUID uuid, String color) {
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

    /*
    public List<GroupTaskAnswer> findEventPeriods(String groupId, int createdById, GroupEventRequest request) {
        String[] ymd = request.day_start().split("-");
        String[] st_ymd = request.day_start().split(":");
        int t = Integer.parseInt(st_ymd[1]);
        int startHalfHourCount = Integer.parseInt(st_ymd[0]) * 2 + (t / 30 + (t % 30 > 0 ? 1 : 0));
        LocalDateTime startTime = LocalDateTime.of(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2]),
                Integer.parseInt(st_ymd[0]), Integer.parseInt(st_ymd[1]));
        ymd = request.day_end().split("-");
        st_ymd = request.day_end().split(":");
        t = Integer.parseInt(st_ymd[1]);
        int endHalfHourCount = 48 - Integer.parseInt(st_ymd[0]) * 2 + (t / 30 + (t % 30 > 0 ? 1 : 0));


        LocalDateTime endTime = LocalDateTime.of(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2]),
                Integer.parseInt(st_ymd[0]), Integer.parseInt(st_ymd[1]));

        LocalDateTime deltaDays = endTime.minusYears(startTime.getYear()).minusDays(startTime.getDayOfYear());
        int countDaysDelta = deltaDays.getDayOfYear();
        if (deltaDays.minusDays(countDaysDelta).getDayOfYear() > 10) throw new IllegalArgumentException("too long day period");


        List<GroupsUsers> users = groupUsersRepository.findAllByGroup(groupsRepository.getById(UUID.fromString(groupId)));

        long[] days = new long[countDaysDelta];
        long mask = (long) ((2L << startHalfHourCount + 1) - 1) << (48 - startHalfHourCount);
        mask = mask | (2L << endHalfHourCount);

        Arrays.fill(days, mask);

        for (GroupsUsers user : users)
            taskService.returnTasksByMonth(user.getUser(), List.of(Privacy.FRIENDS, Privacy.PRIVATE, Privacy.PUBLIC), LocalDateTime start, LocalDateTime end);



    }
    */


    public List<TimeInterval> findEventPeriods(String groupId, int createdById, GroupEventRequest request) {
        // 1. Парсим рабочие часы внутри дня (ожидается формат "HH:mm", например "09:00")
        LocalTime dayStart = LocalTime.parse(request.time_start());
        LocalTime dayEnd = LocalTime.parse(request.time_end());

        // Парсим даты начала и конца периода
        LocalDate startDate = LocalDate.parse(request.day_start());
        LocalDate endDate = LocalDate.parse(request.day_end());

        long timePickMillis = request.time_pick(); // Минимальная длительность свободного окна в мс
        ZoneId zone = ZoneId.systemDefault();      // Часовой пояс для перевода дат в Instant
        // Глобальные рамки всего поиска
        Instant globalStart = startDate.atStartOfDay(zone).toInstant();
        Instant globalEnd = endDate.atTime(LocalTime.MAX).atZone(zone).toInstant();

        // Защита от слишком большого диапазона дат (аналог вашей проверки)
        if (Period.between(startDate, endDate).getDays() > 100) {
            throw new IllegalArgumentException("Too long day period (max 10 days)");
        }

        // 2. Получаем список всех пользователей группы
        List<GroupsUsers> users = groupUsersRepository.findAllByGroup(groupsRepository.getById(UUID.fromString(groupId)));
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        // ОПТИМИЗАЦИЯ: собираем ID всех пользователей, чтобы сделать ОДИН запрос к БД вместо цикла
        List<Integer> userIds = users.stream()
                .map(u -> u.getUser().getUserId())
                .collect(Collectors.toList());

        // Получаем ВСЕ задачи для ВСЕХ пользователей за этот период одним запросом
        // (Рекомендуется добавить такой метод в taskService взамен returnTasksByMonth в цикле)
        List<TaskAnswer> allTasks = taskService.returnTasksForUsersInPeriod(
                userIds,
                List.of(Privacy.FRIENDS, Privacy.PRIVATE, Privacy.PUBLIC),
                globalStart,
                globalEnd
        );

        // 3. Формируем список всех "занятых" интервалов
        List<TimeInterval> busyIntervals = new ArrayList<>();

        // Добавляем реальные задачи пользователей
        for (TaskAnswer task : allTasks) {
            busyIntervals.add(new TimeInterval(task.start(), task.time_end()));
        }

        // Добавляем нерабочие часы для каждого дня как "занятые интервалы"
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Instant startOfToday = date.atStartOfDay(zone).toInstant();
            Instant endOfToday = date.plusDays(1).atStartOfDay(zone).toInstant();

            Instant workingHoursStart = date.atTime(dayStart).atZone(zone).toInstant();
            Instant workingHoursEnd = date.atTime(dayEnd).atZone(zone).toInstant();

            // Время от 00:00 до day_start — занято
            if (workingHoursStart.isAfter(startOfToday)) {
                busyIntervals.add(new TimeInterval(startOfToday, workingHoursStart));
            }
            // Время от day_end до 00:00 следующего дня — занято
            if (endOfToday.isAfter(workingHoursEnd)) {
                busyIntervals.add(new TimeInterval(workingHoursEnd, endOfToday));
            }
        }

        // 4. Сортируем все занятые интервалы строго по времени начала
        busyIntervals.sort(Comparator.comparing(TimeInterval::start));

        // 5. Линейный поиск свободных окон (Алгоритм с maxEnd)
        List<TimeInterval> freeIntervals = new ArrayList<>();
        Instant maxEnd = globalStart; // Указатель текущего окончания занятого времени

        for (TimeInterval busy : busyIntervals) {
            // Если начало текущей задачи позже, чем наш максимальный конец предыдущих задач,
            // значит, мы наткнулись на "дыру" (свободное время)
            if (busy.start().isAfter(maxEnd)) {
                long gap = busy.start().toEpochMilli() - maxEnd.toEpochMilli();
                if (gap >= timePickMillis) {
                    freeIntervals.add(new TimeInterval(maxEnd, busy.start()));
                }
            }

            // Расширяем maxEnd, только если текущая задача заканчивается позже всего, что мы видели
            if (busy.end().isAfter(maxEnd)) {
                maxEnd = busy.end();
            }
        }

        // 6. Проверяем финальный отрезок времени после самой последней задачи до конца периода
        if (globalEnd.isAfter(maxEnd)) {
            long gap = globalEnd.toEpochMilli() - maxEnd.toEpochMilli();
            if (gap >= timePickMillis) {
                freeIntervals.add(new TimeInterval(maxEnd, globalEnd));
            }
        }

        // 7. Маппинг и возврат результата
        // Здесь вы можете превратить List<TimeInterval> в ваш List<GroupTaskAnswer>, если это необходимо
        return freeIntervals;
    }

    public List<GroupTaskAnswer> mapIntervalsToGroupTasks(List<TimeInterval> freeIntervals,
                                                          long timePickMillis,
                                                          String groupId,
                                                          User createdBy,  GroupEventRequest request) {
        return freeIntervals.stream()
                .map(interval -> {
                    Instant taskStart = interval.start();
                    Instant taskEnd = taskStart.plusMillis(timePickMillis);
                    UUID uuid = createGroupTask(new GroupTaskRequest(request.title(),
                            request.body(),
                            createdBy.getUsername(),
                            Status.ACTIVE,
                            Privacy.PUBLIC,
                            request.colour(),
                            taskService.toLocalDateTime(taskStart),
                            taskService.toLocalDateTime(taskEnd),
                            1,
                            UUID.fromString(groupId)
                    ));

                    return new GroupTaskAnswer(
                            uuid.toString(),                       // Генерация уникального ID для предложения
                            request.title(),                       // Заголовок по умолчанию
                            request.body(),                        // Описание по умолчанию
                            groupId,
                            taskStart,                             // Задача начинается ровно в начале свободного окна
                            taskEnd,                               // Длится ровно time_pick миллисекунд
                            Status.ACTIVE,                         // Статус по умолчанию
                            request.importance(),                  // Важность (importance)
                            request.colour(),                      // Цвет карточки по умолчанию
                            null,                                  // vote_id (заполняется при создании голосования)
                            createdBy.getUserId()
                    );
                })
                .collect(Collectors.toList());
    }

    public record TimeInterval(Instant start, Instant end) {
        public long getDurationMillis() {
            return end.toEpochMilli() - start.toEpochMilli();
        }
    }
}


