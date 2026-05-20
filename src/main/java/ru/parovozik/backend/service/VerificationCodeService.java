package ru.parovozik.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeService {

    private final Map<String, VerificationCode> codes = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Value("${app.verification.code.length}")
    private int codeLength;

    @Value("${app.verification.code.expiration-minutes}")
    private int expirationMinutes;

    public String generateCode(String email) {
        String code = generateRandomCode();
        codes.put(email, new VerificationCode(code, Instant.now().plusSeconds(expirationMinutes * 60L)));
        return code;
    }

    public boolean validateCode(String email, String code) {
        VerificationCode storedCode = codes.get(email);
        if (storedCode == null) {
            return false;
        }

        if (storedCode.isExpired()) {
            codes.remove(email);
            return false;
        }

        return storedCode.getCode().equals(code);
    }

    public void removeCode(String email) {
        codes.remove(email);
    }

    private String generateRandomCode() {
        int min = (int) Math.pow(10, codeLength - 1);
        int max = (int) Math.pow(10, codeLength) - 1;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

    private static class VerificationCode {
        private final String code;
        private final Instant expiresAt;

        public VerificationCode(String code, Instant expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }

        public String getCode() {
            return code;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}