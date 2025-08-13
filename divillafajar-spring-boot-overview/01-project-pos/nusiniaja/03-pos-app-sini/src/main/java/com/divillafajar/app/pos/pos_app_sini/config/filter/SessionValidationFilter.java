package com.divillafajar.app.pos.pos_app_sini.config.filter;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


public class SessionValidationFilter extends OncePerRequestFilter {
    @Autowired
    private UserSessionLogRepository sessionLogRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SessionValidationFilter.doInternalFilter");
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            HttpSession session = request.getSession(false);
            System.out.println("Session "+session.getCreationTime());
            if (session != null) {
                String sessionId = session.getId();

                Optional<UserSessionLog> logOpt = sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE");
                if (logOpt.isPresent()) {
                    System.out.println("logOpt ditemukan");
                    // Jika session log ditemukan
                    UserSessionLog log = logOpt.get();
                    log.setStatus("LOGOUT");
                    log.setLogoutTime(LocalDateTime.now());
                    sessionLogRepo.save(log);
                } else {
                    // session tidak valid di DB → logout user
                    System.out.println("logOpt tidak ditemukan");
                    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                    logoutHandler.logout(request, response, auth);

                    // redirect ke login dengan parameter expired
                    response.sendRedirect("/login?expired");
                    return;
                }
            }
        }
        else {
            System.out.println("Auth is NULL");
        }

        filterChain.doFilter(request, response);
    }
}
