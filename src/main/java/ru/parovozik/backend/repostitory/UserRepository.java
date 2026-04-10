package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.parovozik.backend.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    public User findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :name")
    public User find(@Param("name") String name);

}