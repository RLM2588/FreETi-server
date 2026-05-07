package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Task extends TaskTemplate{
    @Id
    @Generated(event = EventType.INSERT)
    @Column(
        name = "id",
        updatable = false,
        nullable = false,
        columnDefinition = "UUID DEFAULT uuidv7()"
    )
    private UUID id;

    @Column(length = 80)
    private String title;

    @ManyToOne(optional = true)
    private RepeatTask repeatTask = null;

    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private PushTemplate pushTemplate;

    public Task() {
    }

    public Task(String title, String body, LocalDateTime createdAt, boolean isEdited, LocalDateTime start, LocalDateTime end, Status status, Privacy privacy, Color color, UUID id, RepeatTask repeatTask, User userId, PushTemplate pushTemplate) {
        super(body, createdAt, isEdited, start, end, status, privacy, color);
        this.title = title;
        this.id = id;
        this.repeatTask = repeatTask;
        this.user = userId;
        this.pushTemplate = pushTemplate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RepeatTask getRepeatTask() {
        return repeatTask;
    }

    public void setRepeatTask(RepeatTask repeatTask) {
        this.repeatTask = repeatTask;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User userId) {
        this.user = userId;
    }

    public PushTemplate getPushTemplate() {
        return pushTemplate;
    }

    public void setPushTemplate(PushTemplate pushTemplate) {
        this.pushTemplate = pushTemplate;
    }
}
