package ru.parovozik.backend.dto;

import java.time.LocalDateTime;

public record UpdateTime(String title, String username, LocalDateTime start, LocalDateTime end, LocalDateTime newStart, LocalDateTime newEnd) {
}
