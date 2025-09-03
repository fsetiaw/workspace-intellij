package com.divillafajar.app.pos.pos_app_sini.controller.thyme.user.admin;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto.ClientDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.service.client.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("admin")
public class ThymeAdminController {

    private final ClientService clientService;
    private LocaleResolver localeResolver;

    public ThymeAdminController(ClientService clientService, LocaleResolver localeResolver) {
        this.clientService=clientService;
        this.localeResolver=localeResolver;
    }
/*
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        System.out.println("GOTO MASTER HOME");
        UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
        model.addAttribute("userLogInfo", userLogInfo);
        //return "form/add-new-client";

        return "admin/index";
    }

 */

    @GetMapping("/home")
    public String showAdminHome(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "pid", required = true) String pid,
            @RequestParam(name = "add", required = false) Boolean add,
            //@RequestParam(name = "lang", required = false) String lang,
            Model model
    ) {
        System.out.println("showAdminHome IS CALLED = " + pid);
        /*
        if (lang != null) {
            Locale locale = new Locale(lang);
            localeResolver.setLocale(request, response, locale);
        }

         */

        ClientDTO client = clientService.getClientDetails(pid);
        List<ClientAddressEntity> storesLocation = client.getClientAddresses();
        model.addAttribute("isAdd", add);
        model.addAttribute("pid",pid);
        //model.addAttribute("lang",lang);
        model.addAttribute("client", client);
        model.addAttribute("ourStores", storesLocation);
        //System.out.println("val lang = " + model.getAttribute("lang"));
        //return "super/clients/home/home-client";
        return "admin/index-admin";
    }

}
