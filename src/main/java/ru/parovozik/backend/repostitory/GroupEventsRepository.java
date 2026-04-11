package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupEvents;

import java.util.UUID;

@RepositoryRestResource(path = "group_events")
public interface GroupEventsRepository extends CrudRepository<GroupEvents, UUID> {
}
