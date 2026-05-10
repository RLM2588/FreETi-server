package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends CrudRepository<Task, UUID> {
    void deleteAllByUser(User user);

    Task findByTitleAndStartAndEndingAndUser(String title, LocalDateTime start, LocalDateTime ending, User user);
    Optional<Task> findByClientUuidAndUser(UUID client_uuid, User user);


    List<Task> findByUserAndEndingBetweenAndRepeatTaskIsNull(User user, LocalDateTime start, LocalDateTime end);
    List<Task> findAllByUserAndRepeatTask(User user, RepeatTask repeatTask);

    List<Task> findByUserAndEndingBetweenAndRepeatTaskIn(User user, LocalDateTime endingAfter, LocalDateTime endingBefore, Collection<RepeatTask> repeatTasks);

}
