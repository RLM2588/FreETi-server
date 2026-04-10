package ru.parovozik.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.model.Status;

import java.time.LocalDateTime;

@MappedSuperclass
public class TaskTemplate {

    @Column(length = 80)
    private String title;

    @Column(length = 100)
    private String body;

    @CurrentTimestamp(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public TaskTemplate() {
    }
}
