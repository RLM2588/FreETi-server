package ru.parovozik.backend.repostitory;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.parovozik.backend.entity.Vote;

@RepositoryRestResource(path="vote")
public interface VoteRepository extends CrudRepository<Vote,Long> {
}
