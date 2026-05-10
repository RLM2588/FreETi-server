package ru.parovozik.backend.dto;

import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Instant;
import java.util.UUID;

public record TaskAnswer(UUID id, String title, String body, Status status, Privacy privacy, String colour, Instant start, Instant time_end, int pushTemplate, int importance, Instant updated_at) {
}
