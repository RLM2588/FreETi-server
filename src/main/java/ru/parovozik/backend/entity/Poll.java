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

    @Column(nullable = false)
    private String var1;

    @Column(nullable = false)
    private String var2;

    @Column
    private String var3;

    @Column
    private String var4;

    @Column
    private String var5;


    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="voting_id")
    private GroupEvents groupEvent;

    public Poll() {
    }

    public Poll(UUID id, String title, String var1, String var2, String var3, String var4, String var5, Status status, GroupEvents groupEvent) {
        this.id = id;
        this.title = title;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
        this.var5 = var5;
        this.status = status;
        this.groupEvent = groupEvent;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getVar4() {
        return var4;
    }

    public void setVar4(String var4) {
        this.var4 = var4;
    }

    public String getVar5() {
        return var5;
    }

    public void setVar5(String var5) {
        this.var5 = var5;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public GroupEvents getGroupEvent() {
        return groupEvent;
    }

    public void setGroupEvent(GroupEvents groupEvent) {
        this.groupEvent = groupEvent;
    }
}
