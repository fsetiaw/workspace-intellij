package com.divillafajar.app.pos.pos_app_sini.config.session;

import com.divillafajar.app.pos.pos_app_sini.repo.UserSessionHistoryRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionListener implements HttpSessionListener {

    @Autowired
    private UserSessionHistoryRepo historyRepo;

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
        String sessionId = se.getSession().getId();
        historyRepo.findAll().stream()
                .filter(h -> h.getSessionId().equals(sessionId) && h.isActive())
                .forEach(h -> {
                    h.setLogoutTime(LocalDateTime.now());
                    h.setActive(false);
                    historyRepo.save(h);
                });
        System.out.println("Before session expired, user: , role: " + role);

        // Misal: simpan ke log, database, dsb.
    }
}
