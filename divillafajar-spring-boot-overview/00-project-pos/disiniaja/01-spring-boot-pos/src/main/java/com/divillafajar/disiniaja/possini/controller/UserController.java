package com.divillafajar.disiniaja.possini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/leaders")
    public String showLeadersHome() {
        return "leaders-home";
    }

    @GetMapping("/systems")
    public String showAdminsHome() {
        return "admin-home";
    }
}
