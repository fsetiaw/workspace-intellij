package com.divillafajar.app.pos.pos_app_sini.controller.user.superuser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("superuser")
public class ThymeSuperuserController {
    @GetMapping("/home")
    public String showHome(Model model) {
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";
        return "super/index.html";
    }

    @GetMapping("/clients")
    public String showAllClients(Model model) {
        //model.addAttribute("userForm", new UserForm());
        //return "form/add-new-client";
        return "super/clients/index-clients.html";
    }
}
