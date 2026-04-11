package ru.parovozik.backend.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Period;
@Entity
@Table
public class PushTemplate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int userId;

    @Column(name = "before_how_days",columnDefinition = "interval")
    private Period beforeHowDays;

    @Column(name = "before_how_hours",columnDefinition = "interval")
    private Duration beforeHowHours;

    public PushTemplate() {
    }

    public PushTemplate(int userId, Period beforeHowDays, Duration beforeHowHours) {
        this.userId = userId;
        this.beforeHowDays = beforeHowDays;
        this.beforeHowHours = beforeHowHours;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
