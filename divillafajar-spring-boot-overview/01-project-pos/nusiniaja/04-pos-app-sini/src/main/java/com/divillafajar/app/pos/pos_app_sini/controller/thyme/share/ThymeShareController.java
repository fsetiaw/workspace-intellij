package com.divillafajar.app.pos.pos_app_sini.controller.thyme.share;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("shared")
public class ThymeShareController {
    private LocaleResolver localeResolver;

    public ThymeShareController(LocaleResolver localeResolver) {
        this.localeResolver=localeResolver;
    }

    @GetMapping("/changeLanguage")
    public String changeLanguage(@RequestParam("lang") String lang,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        Locale locale = new Locale(lang);
        localeResolver.setLocale(request, response, locale); // set session locale
        // redirect ke halaman sebelumnya
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/logout");
    }
}
