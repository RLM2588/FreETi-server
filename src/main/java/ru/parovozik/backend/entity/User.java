package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.generator.EventType;

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

    @Column(length =  80)
    private String username;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;

    @OneToMany(mappedBy="userId")
    private Set<Task> tasks;

    @OneToMany(mappedBy = "userId")
    private Set<RepeatTask> repeatTasks;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="Contacts",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="contactsId")
    )
    private Set<User> friends = new HashSet<User>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="Contacts",
            joinColumns=@JoinColumn(name="contactsId"),
            inverseJoinColumns=@JoinColumn(name="userId")
    )
    private Set<User> friendsOf = new HashSet<User>();

    public User() {
    }

    public User(int userId, String username, String email, String password, LocalDateTime createdAt, Avatar avatar, Set<User> friends, Set<User> friendsOf) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.avatar = avatar;
        this.friends = friends;
        this.friendsOf = friendsOf;
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

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFriendsOf() {
        return friendsOf;
    }

    public void setFriendsOf(Set<User> friendsOf) {
        this.friendsOf = friendsOf;
    }
}
