package ru.parovozik.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.parovozik.backend.entity.PushTemplate;
import ru.parovozik.backend.model.Color;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskRequest(UUID id, String title, String body, Status status, Privacy privacy, String colour, Instant start, Instant time_end, int pushTemplate, int importance, Instant updated_at) {
}