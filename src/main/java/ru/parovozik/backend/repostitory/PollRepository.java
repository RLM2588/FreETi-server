package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.Poll;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "poll")
public interface PollRepository extends CrudRepository<Poll, UUID> {
    void deleteAllByGroupEvent(GroupEvents groupEvent);

    List<Poll> findAllByGroupEvent(GroupEvents groupEvent);

    List<Poll> findAllByGroupEventIn(List<GroupEvents> groupEventsList);

    Poll findPollById(UUID id);
}
