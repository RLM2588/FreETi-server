package ru.parovozik.backend.service;

import org.springframework.stereotype.Service;
import ru.parovozik.backend.entity.Avatar;
import ru.parovozik.backend.repostitory.AvatarRepository;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public boolean create(String link) {
        try {
            Avatar avatar = new Avatar();
            avatar.setLink(link);
            avatarRepository.save(avatar);
            return true;
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public boolean delete(String link) {
        try{
            Avatar avatar = avatarRepository.findByLink(link);
            avatarRepository.delete(avatar);return true;
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
