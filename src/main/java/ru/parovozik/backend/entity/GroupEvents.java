package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.dto.GroupTaskAnswer;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table
public class GroupEvents extends TaskTemplate {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private User createByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Groups group;

    @ManyToOne(fetch = FetchType.LAZY)
    private PushTemplate pushTemplate;

    public PushTemplate getPushTemplate() {
        return pushTemplate;
    }

    public void setPushTemplate(PushTemplate pushTemplate) {
        this.pushTemplate = pushTemplate;
    }

    public GroupEvents() {
    }

    public GroupEvents(String title, String body, LocalDateTime createdAt, boolean isEdited, LocalDateTime start, LocalDateTime end, Status status, Privacy privacy, String color, UUID id, User createByUser, Groups groupId) {
        super(body, createdAt, isEdited, start, end, status, privacy, color);
        this.title = title;
        this.id = id;
        this.createByUser = createByUser;
        this.group = groupId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(User createByUser) {
        this.createByUser = createByUser;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups groupId) {
        this.group = groupId;
    }

    public static GroupTaskAnswer toGroupTaskAnswer(GroupEvents groupEvents) {
        return new GroupTaskAnswer(
                groupEvents.id.toString(),
                groupEvents.title,
                groupEvents.getBody(),
                groupEvents.group.getId().toString(),
                toInstant(groupEvents.getStart()),
                toInstant(groupEvents.getEnd()),
                groupEvents.getStatus(),
                groupEvents.getImportance(),
                groupEvents.getColor(),
                "",
                groupEvents.createByUser.getUserId());
    }

    private static Instant toInstant(LocalDateTime start) {
        return start.toInstant(ZoneOffset.UTC);
    }
}
