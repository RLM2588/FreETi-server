package ru.parovozik.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/swagger-ui.html")
    public String redirectToSwaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}