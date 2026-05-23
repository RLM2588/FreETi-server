package ru.parovozik.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

public record GroupAnswer(java.util.UUID id, String title, String body, boolean is_deleted) {
}
