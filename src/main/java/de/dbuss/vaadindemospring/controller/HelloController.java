package de.dbuss.vaadindemospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    String hello() {
        return "hello";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Spring Boot with Thymeleaf!");
        return "welcome";
    }
}