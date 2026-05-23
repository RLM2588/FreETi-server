package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Entity
@Table
public class RepeatTask extends TaskTemplate{
    @Id
    @Generated(event = EventType.INSERT)
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "UUID DEFAULT uuidv7()"
    )
    private UUID id;

    @Column(length = 80,unique = true)
    private String title;

    @Column(name="globalEnd")
    private LocalDateTime globalEnd;

    @Column(name = "before_how_days",columnDefinition = "interval")
    private Period beforeHowDays;

    @Column(name = "before_how_hours",columnDefinition = "interval")
    private Duration beforeHowHours;

    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private PushTemplate pushTemplate;

    public RepeatTask() {
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

    public RepeatTask(String title, String body, LocalDateTime createdAt, boolean isEdited, LocalDateTime start, LocalDateTime end, Status status, Privacy privacy, String color, UUID id, LocalDateTime globalEnd, Period beforeHowDays, Duration beforeHowHours, User userId, PushTemplate pushTemplate) {
        super(body, createdAt, isEdited, start, end, status, privacy, color);
        this.title = title;
        this.id = id;
        this.globalEnd = globalEnd;
        this.beforeHowDays = beforeHowDays;
        this.beforeHowHours = beforeHowHours;
        this.user = userId;
        this.pushTemplate = pushTemplate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getGlobalEnd() {
        return globalEnd;
    }

    public void setGlobalEnd(LocalDateTime globalEnd) {
        this.globalEnd = globalEnd;
    }

    public Period getBeforeHowDays() {
        return beforeHowDays;
    }

    public void setBeforeHowDays(Period beforeHowDays) {
        this.beforeHowDays = beforeHowDays;
    }

    public Duration getBeforeHowHours() {
        return beforeHowHours;
    }

    public void setBeforeHowHours(Duration beforeHowHours) {
        this.beforeHowHours = beforeHowHours;
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
