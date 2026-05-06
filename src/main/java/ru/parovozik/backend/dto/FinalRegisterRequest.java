package ru.parovozik.backend.dto;

import jakarta.validation.constraints.*;

public class FinalRegisterRequest {
    @NotBlank
    private String login;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String code;
    @NotBlank @Size(min = 6)
    private String password;

    // Геттеры и сеттеры
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}