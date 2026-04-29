package ru.parovozik.backend.controllers;

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

//    @GetMapping("/test")
//    public Map<String, String>

    // POST-запрос для тестирования отправки данных
    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> request) {
        return Map.of(
                "received", request,
                "timestamp", System.currentTimeMillis()
        );
    }

    // Простой GET с параметром
    @GetMapping("/hello2")
    public String hello2(@RequestParam(defaultValue = "World") String name) {
        System.out.println("niggas");
        return "Hello, " + name + "! Connection is secure!";
    }

//    @PostMapping("/hello")
//    public Map<String, String> hello(@RequestBody Map<String, String> request) {
//        return Map.of("input_text", "Hello, " + request.get("out_text") + "! Connection is secure!");
//    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("input_text", "Hello, " + "nigga" + "! Connection is secure!");
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestBody Map<String, String> arg) {
        //for (String s : arg.keySet())
        //    System.out.println(s);arg.get("out_text")
        System.out.println("aa");
        return Map.of("input_text", "Hello, " + arg.get("out_text") + "! Connection is secure!");
    }
}