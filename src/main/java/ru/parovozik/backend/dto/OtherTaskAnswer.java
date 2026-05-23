package ru.parovozik.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.parovozik.backend.model.Privacy;
import ru.parovozik.backend.model.Status;

import java.time.Instant;
import java.util.UUID;

public record OtherTaskAnswer(UUID id,
                              String title,
                              String body,
                              int user_id,
                              Status status,
                              Privacy privacy,
                              String colour,
                              @JsonFormat(shape = JsonFormat.Shape.NUMBER) Instant start,
                              @JsonFormat(shape = JsonFormat.Shape.NUMBER) Instant time_end,
                              int pushTemplate,
                              int importance) {
}
