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
    @CurrentTimestamp(event = {EventType.INSERT, EventType.UPDATE})
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

    public LocalDateTime getEnding() {
        return ending;
    }

    public void setEnding(LocalDateTime ending) {
        this.ending = ending;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column
    private int importance;

    @Column
    private String color;

    public TaskTemplate(String body, LocalDateTime createdAt, boolean isEdited, LocalDateTime start, LocalDateTime end, Status status, Privacy privacy, String color) {
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

    public String getColor() {
        return color;
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
