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

        System.out.println("SessionValidationFilter.doInternalFilter");
        String path = request.getServletPath();
        System.out.println("doInternalFilter path = "+path);
        System.out.println("doInternalFilter path = "+request.getContextPath());

        if ("/logout".equals(path)||"/customer/login".equals(path)||"/customer/processLoginForm".equals(path)
               ||"/login".equals(path)||"/session-expired".equals(path)) {
        //if(!isSessionValid(request)){
            System.out.println("isSessionValid tidak valid");
            //response.sendRedirect("qrview");
            filterChain.doFilter(request, response);
            return;
        }
        else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                HttpSession session = request.getSession(false);
                System.out.println("Session "+session.getCreationTime());
                System.out.println("Auth "+auth.getAuthorities());
                if (session != null) {
                    String sessionId = session.getId();

                    Optional<UserSessionLog> logOpt = sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE");
                    //if(false) {
                    if (logOpt.isPresent()) {
                        System.out.println("logOpt ditemukan");
                        // Jika session log ditemukan
                        UserSessionLog log = logOpt.get();
                        //log.setLastAccessTime("LOGOUT");
                        log.setLastAccessTime(LocalDateTime.now());
                        sessionLogRepo.save(log);
                    } else {
                        // session tidak valid di DB → logout user
                        System.out.println("logOpt tidak ditemukan");
                        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
                        logoutHandler.logout(request, response, auth);

                        // redirect ke login dengan parameter expired
                        response.sendRedirect("aneh/login?expired");
                        return;
                    }
                }
            }
            else {
                System.out.println("Auth is NULL="+request.getContextPath());
                if ("/logout".equals(path)||"/customer/login".equals(path)||"/customer/processLoginForm".equals(path)
                        ||"/session-expired".equals(path)) {
                    filterChain.doFilter(request, response);
                    response.sendRedirect(request.getContextPath() + "/customer/login");
                    //return;
                }
                System.out.println("Auth is NULL else");
                response.sendRedirect(request.getContextPath() + "/customer/login");
                return;
                //response.sendRedirect("customer/main-login");
            }
        }

        System.out.println("filterChain.doFilter(request, response)");
        filterChain.doFilter(request, response);
    }

    private boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false = jangan buat session baru
        if (session == null) {
            return false; // tidak ada session sama sekali
        }

        // Ambil Authentication dari SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // tidak ada auth atau belum login
        }

        // Pastikan bukan anonymous user
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return true; // kalau lolos semua, berarti valid
    }
}
