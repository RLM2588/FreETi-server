package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.util.Set;
import java.util.UUID;

@Entity
@Table
public class Groups {
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

    @Column(length=100)
    private String body;

    public Set<GroupEvents> getGroupEventsSet() {
        return groupEventsSet;
    }

    public void setGroupEventsSet(Set<GroupEvents> groupEventsSet) {
        this.groupEventsSet = groupEventsSet;
    }

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY)
    private Set<GroupEvents> groupEventsSet;


    public Groups() {
    }

    public Groups(UUID id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public GroupAnswer toGroupAnswer() {return new GroupAnswer(id, title, body, false); }
}
