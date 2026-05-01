package ru.parovozik.backend.repostitory;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Poll;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.entity.Vote;

import java.util.List;

@RepositoryRestResource(path="vote")
public interface VoteRepository extends CrudRepository<Vote,Long> {
    List<Vote> findAllByUserID(User user);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vote v SET v.isLeft = false WHERE v.userID = :userId")
    void reassignVotesToDefaultUser(@Param("userId") User user);


    void deleteAllByPoll(Poll poll);
}


