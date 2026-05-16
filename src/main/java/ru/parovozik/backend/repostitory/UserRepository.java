package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.User;

import java.util.List;

@RepositoryRestResource(path = "user")
public interface UserRepository extends CrudRepository<User,Integer> {
    User findUserByUsername(String username);

    User findUserByUserId(int userId);

    List<User> findByNameContainingIgnoreCase(String namePart);

    List<User> findByUsernameContainingIgnoreCase(String usernamePart);

    User findByEmail(String email);
}