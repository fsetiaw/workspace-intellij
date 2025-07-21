package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class LoginController {
    @GetMapping("/hello")
    public String hello(Model theModel) {
        theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        return  "hellopage";
    }
}
