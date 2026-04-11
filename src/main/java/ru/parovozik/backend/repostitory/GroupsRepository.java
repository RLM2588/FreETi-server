package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Groups;

import java.util.UUID;

@RepositoryRestResource(path = "groups")
public interface GroupsRepository extends CrudRepository<Groups, UUID> {
}
