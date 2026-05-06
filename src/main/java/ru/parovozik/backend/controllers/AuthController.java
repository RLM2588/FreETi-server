package ru.parovozik.backend.controllers;

import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/test")
    public ResponseEntity<?> testRegister(@RequestBody RegisterRequest request) {
        // В текущем RegisterRequest уже есть username и email
        authService.sendVerificationCode(request.getUsername(), request.getEmail());
        return ResponseEntity.ok("Code sent to " + request.getEmail());
    }

    @PostMapping("/final_register")
    public ResponseEntity<?> finalRegister(@Valid @RequestBody FinalRegisterRequest request) {
        TokenResponse tokens = authService.completeRegistration(request);
        return ResponseEntity.ok(tokens);
    }


    // Соответствует методу register(username, email) в Android
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.initiateRegistration(request);
        return ResponseEntity.ok("Verification code sent");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse tokens = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out");
    }
}