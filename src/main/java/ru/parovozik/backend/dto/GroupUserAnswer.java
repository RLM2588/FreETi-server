package ru.parovozik.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.parovozik.backend.model.Role;

public record GroupUserAnswer(String group, int user1, Role role) {
}
