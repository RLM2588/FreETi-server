package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.util.UUID;

@Entity
@Table(name="contacts")
public class Contact {
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne
    private User first;
    @ManyToOne
    private User second;
    @Column
    private boolean isFriend;

    public Contact() {
    }

    public Contact(UUID uuid, User first, User second, boolean isFriend) {
        this.uuid = uuid;
        this.first = first;
        this.second = second;
        this.isFriend = isFriend;
    }

    public Contact(User first, User second, boolean isFriend) {
        this.first = first;
        this.second = second;
        this.isFriend = isFriend;
    }

    public User getFirst() {
        return first;
    }

    public void setFirst(User first) {
        this.first = first;
    }

    public User getSecond() {
        return second;
    }

    public void setSecond(User second) {
        this.second = second;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}

