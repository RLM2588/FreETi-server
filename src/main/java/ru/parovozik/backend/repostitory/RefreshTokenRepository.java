package ru.parovozik.backend.repostitory;

import ru.parovozik.backend.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import ru.parovozik.backend.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}