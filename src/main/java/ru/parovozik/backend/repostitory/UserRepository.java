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

    User findByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM user_contacts WHERE user_id = :userId OR contact_id = :userId", nativeQuery = true)
    void deleteContacts(@Param("userId") long userId);

    @Query("SELECT DISTINCT c FROM User u JOIN u.friends c WHERE u.userId = :userId")
    List<User> findFriends(@Param("userId") long userId);
}