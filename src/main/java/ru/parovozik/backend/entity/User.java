package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.dto.UserAnswer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name="Users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

    @Column(length =  80, unique = true)
    private String username;

    @Column(length =  80)
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @Column(length = 4)
    private String avatar;

    @OneToMany(mappedBy="user")
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user")
    private Set<RepeatTask> repeatTasks;

    public User(int userId, String username, String email, String password, LocalDateTime createdAt, String avatar, Set<Task> tasks, Set<RepeatTask> repeatTasks, Set<User> friends, Set<User> friendsOf) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.avatar = avatar;
        this.tasks = tasks;
        this.repeatTasks = repeatTasks;

    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<RepeatTask> getRepeatTasks() {
        return repeatTasks;
    }

    public void setRepeatTasks(Set<RepeatTask> repeatTasks) {
        this.repeatTasks = repeatTasks;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserAnswer asUserAnswer() { return new UserAnswer(this.userId, this.username, this.name, this.avatar); }
}
