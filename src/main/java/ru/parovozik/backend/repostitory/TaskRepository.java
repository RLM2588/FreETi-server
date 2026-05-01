package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends CrudRepository<Task, UUID> {
    void deleteAllByUserId(User user);
}
