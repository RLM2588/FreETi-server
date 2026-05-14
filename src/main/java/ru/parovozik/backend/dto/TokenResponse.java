package ru.parovozik.backend.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken,

    int userId,
    String login) {

}

