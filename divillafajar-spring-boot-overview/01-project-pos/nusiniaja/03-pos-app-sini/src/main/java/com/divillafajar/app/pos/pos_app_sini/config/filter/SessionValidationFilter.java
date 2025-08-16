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
@Component
public class SessionValidationFilter extends OncePerRequestFilter {
    @Autowired
    private UserSessionLogRepository sessionLogRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();


        if ("/logout".equals(path)||"/customer/login".equals(path)||"/customer/processLoginForm".equals(path)
               ||"/login".equals(path)||path.contains("/session-expired")||path.contains("/something-wrong")
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String sessionId = session.getId();
                    Optional<UserSessionLog> sessionExist = sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE");
                    if (sessionExist.isPresent()) {
                        // Jika session log ditemukan, update last accss time
                        UserSessionLog log = sessionExist.get();
                        log.setLastAccessTime(LocalDateTime.now());
                        sessionLogRepo.save(log);
                    } else {
                        /*
                         *  sessionExist tidak ditemukan di DB (kasus bila tipa2 data di db hilang/corrupt
                         */
                        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                        logoutHandler.logout(request, response, auth);
                        response.sendRedirect("/something-wrong");
                        return;
                    }
                }
                else {
                    System.out.println("Session is null");
                }
            }
            else {
                /*
                ** Lewat MaxIdleTime
                 */
                System.out.println("Auth is NULL else");
                response.sendRedirect(request.getContextPath() + "/session-expired");
                //response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


}
