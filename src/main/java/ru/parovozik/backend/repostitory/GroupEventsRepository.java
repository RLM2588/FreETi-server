package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.Groups;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "group_events")
public interface GroupEventsRepository extends CrudRepository<GroupEvents, UUID> {
    void deleteAllByGroup(Groups group);

    List<GroupEvents> findAllByGroup(Groups group);
}
