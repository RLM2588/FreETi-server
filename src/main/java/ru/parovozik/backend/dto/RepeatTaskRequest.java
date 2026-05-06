package ru.parovozik.backend.dto;

import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public record RepeatTaskRequest(String title, String body, String username, Status status, Privacy privacy, Color color, LocalDateTime starting, LocalDateTime ending, PushTemplate pushTemplate, Period period, Duration duration) {
}
