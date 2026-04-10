package ru.parovozik.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping("/docs")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}