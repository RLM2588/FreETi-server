package ru.parovozik.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.Period;
@Entity
@Table
public class PushTemplate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int pushId;

    @Column
    private int userId;

    @Column(name = "before_how_days",columnDefinition = "interval")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Period beforeHowDays;

    @Column(name = "before_how_hours",columnDefinition = "interval")
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration beforeHowHours;

    public PushTemplate() {
    }

    public PushTemplate(int pushId, Period beforeHowDays, Duration beforeHowHours) {
        this.pushId = pushId;
        this.beforeHowDays = beforeHowDays;
        this.beforeHowHours = beforeHowHours;
    }

    public int getPushId() {
        /*return pushId;*/ return 1;
    }

    public void setPushId(int pushId) {
        this.pushId = pushId;
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
