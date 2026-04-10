package ru.parovozik.backend;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // GET-запрос для проверки связи
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of(
                "status", "ok",
                "message", "pong",
                "protocol", "HTTPS is working!"
        );
    }

    // POST-запрос для тестирования отправки данных
    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> request) {
        return Map.of(
                "received", request,
                "timestamp", System.currentTimeMillis()
        );
    }

    // Простой GET с параметром
    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return "Hello, " + name + "! Connection is secure!";
    }
}