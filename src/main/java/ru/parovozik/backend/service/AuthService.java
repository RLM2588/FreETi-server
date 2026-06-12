package ru.parovozik.backend.service;

import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.repostitory.*;
import ru.parovozik.backend.authentication.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class AuthService {
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder, VerificationCodeService verificationCodeService, EmailService emailService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
    }

    @Transactional
    public void initiateRegistration(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        if (!verificationCodeService.canSendNewCode(request.getEmail())) {
            throw new RuntimeException("Wait 1 minute for sending new code");
        }

        String code = verificationCodeService.generateCode(request.getEmail());
        System.out.println("code was generated");

        emailService.sendVerificationCode(request.getEmail(), code, request.getUsername());
        System.out.println("code was sent");
    }

    @Transactional
    public void sendVerificationCode(String username, String email) {
        if (userRepository.findUserByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        String code = verificationCodeService.generateCode(email);
        emailService.sendVerificationCode(email, code, username);
    }

    @Transactional
    public TokenResponse completeRegistration(FinalRegisterRequest request) {
        if (!verificationCodeService.validateCode(request.getEmail(), request.getCode())) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        User user = new User();
        user.setUsername(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        verificationCodeService.removeCode(request.getEmail());

        return generateTokens(user);
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // if (userRepository.findByEmail(request.getEmail()) != null) { ... }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }


    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println(passwordEncoder.encode(request.getPassword()));
            throw new RuntimeException("Invalid credentials");
        }
        return generateTokens(user);
    }

    @Transactional
    public TokenResponse refresh(String refreshTokenValue) {
        System.out.println("token to refresh: " + refreshTokenValue);
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = storedToken.getUser();
        refreshTokenRepository.delete(storedToken);
        return generateTokens(user);
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue)
                .ifPresent(refreshTokenRepository::delete);
    }

    private TokenResponse generateTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        RefreshToken rt = new RefreshToken(refreshToken, user, Instant.now().plusMillis(604800000));
        refreshTokenRepository.save(rt);
        //System.out.println(refreshToken);
        //System.out.println(accessToken);

        return new TokenResponse(accessToken, refreshToken, user.getUserId(), user.getUsername());
    }
}
