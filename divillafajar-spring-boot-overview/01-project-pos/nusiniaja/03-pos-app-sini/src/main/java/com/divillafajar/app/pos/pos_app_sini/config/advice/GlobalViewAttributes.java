package com.divillafajar.app.pos.pos_app_sini.config.advice;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalViewAttributes {

    @ModelAttribute
    public void globalAttributes(HttpSession session, Model model) {
        session.setMaxInactiveInterval(5);
        //model.addAttribute("sessionId", session.getId());
        //model.addAttribute("sessionCreationTime", session.getCreationTime());
        //model.addAttribute("sessionMaxInactiveInternal", session.getMaxInactiveInterval());
        //model.addAttribute("sessionLastAccessedTime", session.getLastAccessedTime());
    }
}