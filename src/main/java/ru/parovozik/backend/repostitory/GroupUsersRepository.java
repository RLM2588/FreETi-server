package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Groups;
import ru.parovozik.backend.entity.GroupsUsers;
import ru.parovozik.backend.entity.User;

import java.util.List;

@RepositoryRestResource(path="group_members")
public interface GroupUsersRepository extends CrudRepository<GroupsUsers,Long> {
    void deleteAllByUser(User user);

    List<GroupsUsers> findAllByUser(User user);

    void deleteAllByGroup(Groups group);

    GroupsUsers findByUserAndGroup(User user, Groups group);
}
