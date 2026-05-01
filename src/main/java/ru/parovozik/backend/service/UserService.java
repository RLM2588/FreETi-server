package ru.parovozik.backend.service;

import jakarta.transaction.Transactional;
import org.apache.juli.logging.Log;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.parovozik.backend.dto.UserAnswer;
import ru.parovozik.backend.dto.UserRequest;
import ru.parovozik.backend.entity.GroupEvents;
import ru.parovozik.backend.entity.RepeatTask;
import ru.parovozik.backend.entity.Task;
import ru.parovozik.backend.entity.User;
import ru.parovozik.backend.repostitory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RepeatTaskRepository repeatTaskRepository;
    private final GroupUsersRepository groupUsersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TaskRepository taskRepository, RepeatTaskRepository repeatTaskRepository, GroupUsersRepository groupUsersRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.repeatTaskRepository = repeatTaskRepository;
        this.groupUsersRepository = groupUsersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean addUser(UserRequest userResponse) {
        try {
            User user = new User();
            user.setUsername(userResponse.username());
            user.setName(userResponse.name());
            user.setEmail(userResponse.email());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public boolean changeName(String username, String newName) {
        try {
            User user = userRepository.findUserByUsername(username);
            user.setName(newName);
            userRepository.save(user);
            return true;
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public UserAnswer getUser(String username) {
        User user = userRepository.findUserByUsername(username);
        return new UserAnswer(user.getName(),user.getUsername(),user.getAvatar().getLink());
    }

    public UserAnswer getUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Неверный email или пароль");
        }
        return new UserAnswer(user.getName(),user.getUsername(),user.getAvatar().getLink());
    }

    public boolean updatePassword(String email, String password, String newPassword) {
        User user = userRepository.findByEmail(email);
        if(user != null && passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        else {
            throw new BadCredentialsException("Неверный email или пароль");
        }
    }

    public List<UserAnswer> findFriend(String username) {
        User user = userRepository.findUserByUsername(username);
        List<UserAnswer> userAnswers = new ArrayList<>();
        if (user != null) {
            List<User> users = userRepository.findFriends(user.getUserId());
            for(User one : users) {
                userAnswers.add(new UserAnswer(user.getName(),user.getUsername(),user.getAvatar().getLink()));
            }
        }
        return userAnswers;
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username);
        taskRepository.deleteAllByUserId(user);
        repeatTaskRepository.deleteAllByUserId(user);
        groupUsersRepository.deleteAllByUser(user);
        userRepository.deleteContacts(user.getUserId());
        userRepository.delete(user);
    }

}
