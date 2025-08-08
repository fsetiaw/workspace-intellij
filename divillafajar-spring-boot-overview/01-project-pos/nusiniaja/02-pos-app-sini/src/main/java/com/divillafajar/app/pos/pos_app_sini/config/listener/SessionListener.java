package com.divillafajar.app.pos.pos_app_sini.config.listener;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        System.out.println("Session destroyed: " + session.getId());

        // Ambil data terakhir dari session
        String role = (String) session.getAttribute("USER_ROLE");
        //String username = (String) session.getAttribute("USERNAME");

        System.out.println("Before session expired, user: , role: " + role);

        // Misal: simpan ke log, database, dsb.
    }
}
