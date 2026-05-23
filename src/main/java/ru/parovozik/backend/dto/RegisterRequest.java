package ru.parovozik.backend.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 6) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 6) String password) {
        this.password = password;
    }
}
