package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.Groups;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "group_events")
public interface GroupEventsRepository extends CrudRepository<GroupEvents, UUID> {
    void deleteAllByGroup(Groups group);

    List<GroupEvents> findAllByGroup(Groups group);

    List<GroupEvents> findAllByGroupAndEndingBetween(Groups groups, LocalDateTime start, LocalDateTime end);
}
