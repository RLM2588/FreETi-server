package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.RepeatTask;

import java.util.UUID;

@RepositoryRestResource(path = "repeat_task")
public interface RepeatTaskRepository extends CrudRepository<RepeatTask, UUID> {
}
