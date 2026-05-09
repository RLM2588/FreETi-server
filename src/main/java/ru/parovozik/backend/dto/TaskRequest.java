package ru.parovozik.backend.dto;

import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.LocalDateTime;

public record TaskRequest(String title, String body, String username,Status status, Privacy privacy, String color, LocalDateTime starting, LocalDateTime ending, PushTemplate pushTemplate,int importance, LocalDateTime updatedA) {
}