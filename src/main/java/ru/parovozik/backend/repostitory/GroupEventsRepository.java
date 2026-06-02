package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.Groups;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "group_events")
public interface GroupEventsRepository extends CrudRepository<GroupEvents, UUID> {
    void deleteAllByGroup(Groups group);

    List<GroupEvents> findAllByGroup(Groups group);

    @Query("SELECT ge FROM GroupEvents ge " +
            "WHERE ge.group = :group " +
            "AND ge.status IN :statuses")
    List<GroupEvents> findAllByGroupWithStatus(@Param("group") Groups group,
                                     @Param("statuses") List<Status> statuses);

    List<GroupEvents> findAllByGroupAndEndingBetween(Groups groups, LocalDateTime start, LocalDateTime end);

    @Query("SELECT ge FROM GroupEvents ge " +
            "WHERE ge.group = :group " +
            "AND ge.ending BETWEEN :start AND :end " +
            "AND ge.status IN :statuses")
    List<GroupEvents> findAllByGroupAndEndingBetweenWithStatus(
            @Param("group") Groups group,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("statuses") List<Status> statuses
    );
}
