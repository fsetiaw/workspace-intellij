package com.divillafajar.app.pos.pos_app_sini.thyme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("welcome")
public class CustomerController {
    @RequestMapping("/login")
    public String showLoginForm(Model theModel) {
        //theModel.addAttribute("theDate", java.time.LocalDateTime.now());
        System.out.println("/loginForm is called");
        return  "customer/loginPage-form";
    }
    @RequestMapping("/processLoginForm")
    public String processForm() {
        return "customer/processLoginForm";
    }
}
