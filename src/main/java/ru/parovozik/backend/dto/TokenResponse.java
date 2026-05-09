package ru.parovozik.backend.dto;

public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    private long userId;
    private String login;

    public TokenResponse(String accessToken, String refreshToken, long userId, String login) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getUserId() { return userId; }
    public String getLogin() { return login; }
}