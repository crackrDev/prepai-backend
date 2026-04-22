package com.prepai.prepai_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//And this class is personally for checking api working or not
@RestController
public class HealthController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
