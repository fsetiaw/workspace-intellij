package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    private UserSessionLogRepository sessionLogRepo;

    private final String redirectUrl;

    public CustomLogoutHandler(@Value("${app.logout.redirect:/login?expired}") String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Hapus security context & invalidate session
        System.out.println("CustomLogoutHandler called");

        if (request.getSession(false) != null) {
            String sessionId = request.getSession().getId();

            // Update status di DB
            sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE").ifPresent(log -> {
                log.setStatus("LOGOUT");
                log.setLogoutTime(LocalDateTime.now());
                sessionLogRepo.save(log);
                System.out.println("Session log updated to LOGOUT");
            });

            // Invalidate session
            request.getSession().invalidate();
        }

        // Bersihkan security context
        SecurityContextHolder.clearContext();
    }
        // Redirect ke URL custom


}
