package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import ru.parovozik.backend.model.Status;

import java.util.UUID;

@Entity
@Table(name="voting")
public class Poll {
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

    @Enumerated(EnumType.STRING)
    private Status status;



}
