package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Task;

import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends CrudRepository<Task, UUID> {
}
