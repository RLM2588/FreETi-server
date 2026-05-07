package ru.parovozik.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.LocalDateTime;

@MappedSuperclass
public class TaskTemplate {

    @Column(length = 100)
    private String body;

    @Column
    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @Column
    private boolean isEdited;

    @Column
    private LocalDateTime start;

    @Column
    private LocalDateTime ending;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    @Enumerated(EnumType.STRING)
    private Privacy privacy;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;

    public TaskTemplate(String body, LocalDateTime createdAt, boolean isEdited, LocalDateTime start, LocalDateTime end, Status status, Privacy privacy, Color color) {
        this.body = body;
        this.createdAt = createdAt;
        this.isEdited = isEdited;
        this.start = start;
        this.ending = end;
        this.status = status;
        this.privacy = privacy;
        this.color = color;
    }

    public TaskTemplate() {
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return ending;
    }

    public void setEnd(LocalDateTime end) {
        this.ending = end;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
