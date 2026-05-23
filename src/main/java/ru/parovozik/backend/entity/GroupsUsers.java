package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import ru.parovozik.backend.dto.GroupUserAnswer;
import ru.parovozik.backend.model.Role;


@Entity
@Table
public class GroupsUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public GroupsUsers() {
    }

    public GroupsUsers(Long id, User user, Groups group, Role role) {
        this.id = id;
        this.user = user;
        this.group = group;
        this.role = role;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public GroupUserAnswer toGroupUserAnswer() { return new GroupUserAnswer(group.getId().toString(), user.getUserId(), role); }
}
