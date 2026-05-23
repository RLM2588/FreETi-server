package ru.parovozik.backend.repostitory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.parovozik.backend.entity.Contact;
import ru.parovozik.backend.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends CrudRepository<Contact, UUID> {
    Contact findAllByFirstAndSecond(User first, User second);
    Contact findAllByFirstAndSecondAndIsFriend(User first, User second, boolean isFriend);

    List<Contact> findAllByFirstAndIsFriend(User first, boolean isFriend);
    List<Contact> findAllBySecondAndIsFriend(User second, boolean isFriend);

    @Modifying
    @Query("UPDATE Contact c SET c.isFriend = :isFriend " +
            "WHERE c.first = :first AND c.second = :second " +
            "AND c.isFriend <> :isFriend")
    void updateIsFriend(@Param("first") User first,
                        @Param("second") User second,
                        @Param("isFriend") boolean isFriend);
    void deleteALlByFirstAndSecond(User first, User second);

    void deleteALlByFirstOrSecond(User first, User second);
}
