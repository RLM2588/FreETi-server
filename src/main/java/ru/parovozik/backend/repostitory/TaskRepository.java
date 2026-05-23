package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Privacy;

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

    // TODO нужно бы искать по тому, находится ли начало в данном промежутке
    List<Task> findByUserAndStartBetweenAndRepeatTaskIsNull(User user, LocalDateTime start, LocalDateTime end);
    List<Task> findAllByUserAndRepeatTask(User user, RepeatTask repeatTask);

    List<Task> findByUserAndEndingBetweenAndRepeatTaskIn(User user, LocalDateTime endingAfter, LocalDateTime endingBefore, Collection<RepeatTask> repeatTasks);


    Optional<Task> findByClientUuid(UUID clientUuid);

    @Query("SELECT t FROM Task t " +
            "WHERE t.user.userId IN :userIds " +
            "AND t.privacy IN :privacies " +
            "AND t.start < :endPeriod " +
            "AND t.ending > :startPeriod" +
            "AND t.importance >= :importanceValue"
    )
    List<Task> findTasksForUsersInPeriod(
            @Param("userIds") List<Integer> userIds,
            @Param("privacies") List<Privacy> privacies,
            @Param("startPeriod") LocalDateTime startPeriod,
            @Param("endPeriod") LocalDateTime endPeriod,
            @Param("importanceValue") int importance
    );
}
