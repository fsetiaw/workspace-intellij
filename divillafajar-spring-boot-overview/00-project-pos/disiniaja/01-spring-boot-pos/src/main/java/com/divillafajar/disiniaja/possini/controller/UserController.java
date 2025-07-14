package com.divillafajar.disiniaja.possini.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




//@Controller
@RestController
@RequestMapping("users")
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

    @GetMapping
    public String getUser() {
        return "get User";
    }

    @PostMapping
    public String createUser() {
        return "create User";
    }

    @PutMapping
    public String updateUser() {
        return "update User";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete User";
    }
}
