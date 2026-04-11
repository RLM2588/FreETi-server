package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.GroupsUsers;

@RepositoryRestResource(path="group_members")
public interface GroupUsersRepository extends CrudRepository<GroupsUsers,Long> {
}
