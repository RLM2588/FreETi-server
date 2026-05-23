package ru.parovozik.backend.repostitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Avatar;

@RepositoryRestResource(path = "avatar")
public interface AvatarRepository extends CrudRepository<Avatar,Integer> {

    Avatar findByLink(String link);
}
