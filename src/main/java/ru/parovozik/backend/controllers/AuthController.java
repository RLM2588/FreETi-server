package ru.parovozik.backend.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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


    @PostMapping("/final_register")
    public ResponseEntity<?> finalRegister(@Valid @RequestBody FinalRegisterRequest request) {
        try {
            TokenResponse tokens = authService.completeRegistration(request);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            System.out.println("final register error");
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.initiateRegistration(request);
            return ResponseEntity.ok("Verification code sent");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            TokenResponse tokens = authService.login(request);
            return ResponseEntity.ok(tokens);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            TokenResponse tokens = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(tokens);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        try {
            authService.logout(request.getRefreshToken());
            return ResponseEntity.ok("Logged out");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}