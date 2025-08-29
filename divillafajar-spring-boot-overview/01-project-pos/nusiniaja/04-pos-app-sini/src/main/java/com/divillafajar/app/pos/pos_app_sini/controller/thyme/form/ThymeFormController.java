package com.divillafajar.app.pos.pos_app_sini.controller.thyme.form;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("form")
public class ThymeFormController {

    @GetMapping("/superuser/register/client")
    public String showForm(Model model) {
        System.out.println("showForm is CALLED");
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";
        return "form/registrasi/client";
    }
}
