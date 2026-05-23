package ru.parovozik.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.parovozik.backend.model.Status;

import java.time.Instant;

public record GroupTaskAnswer(String id,
                              String title,
                              String body,
                              String group_id,
                              @JsonFormat(shape = JsonFormat.Shape.NUMBER) Instant start,
                              @JsonFormat(shape = JsonFormat.Shape.NUMBER) Instant time_end,
                              Status status,
                              int importance,
                              String colour,
                              String vote_id,
                              int creatby_user_id) {
}
