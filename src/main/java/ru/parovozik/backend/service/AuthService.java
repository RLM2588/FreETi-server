package ru.parovozik.backend.service;

import ru.parovozik.backend.dto.*;
import ru.parovozik.backend.entity.*;
import ru.parovozik.backend.repostitory.*;
import ru.parovozik.backend.authentication.JwtTokenProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // проверка email, если нужно:
        // if (userRepository.findByEmail(request.getEmail()) != null) { ... }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }


    @Transactional
    public void initiateRegistration(RegisterRequest request) {
        if (userRepository.findUserByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Генерация кода (например, 6 цифр)
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        verificationCodes.put(request.getEmail(), code);

        // Тут должна быть отправка письма. Пока выводим в консоль:
        System.out.println("Код подтверждения для " + request.getEmail() + ": " + code);
    }

    @Transactional
    public void sendVerificationCode(String username, String email) {
        if (userRepository.findUserByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        verificationCodes.put(email, code);

        System.out.println("Код для " + email + ": " + code);
    }

    @Transactional
    public TokenResponse completeRegistration(FinalRegisterRequest request) {
        String validCode = verificationCodes.get(request.getEmail());
        if (validCode == null || !validCode.equals(request.getCode())) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        User user = new User();
        user.setUsername(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        verificationCodes.remove(request.getEmail());
        return generateTokens(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return generateTokens(user);
    }

    @Transactional
    public TokenResponse refresh(String refreshTokenValue) {
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