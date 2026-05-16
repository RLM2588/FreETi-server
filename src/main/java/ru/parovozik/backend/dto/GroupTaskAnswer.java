package ru.parovozik.backend.dto;

import ru.parovozik.backend.model.Status;

import java.time.Instant;
import java.time.LocalDateTime;

public record GroupTaskAnswer(String id,
                              String title,
                              String body,
                              String group_id,
                              Instant start,
                              Instant end,
                              Status status,
                              int importance,
                              String colour,
                              String vote_id,
                              int creatby_user_id) {
}
