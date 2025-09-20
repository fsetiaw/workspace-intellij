package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import com.divillafajar.app.pos.pos_app_sini.model.customer.AuthenticatedCustomerModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

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
        //request.setAttribute("user_role", request.getAttribute("USER_ROLE"));
        HttpSession session = request.getSession(false); // false = jangan bikin baru kalau ga ada

        if (session != null) {
            //Object role = session.getAttribute("USER_ROLE");
            //System.out.println("Role dari session = " + role);
            UserSessionLog userLogInfo = (UserSessionLog) session.getAttribute("userLogInfo");
            request.setAttribute("userLogInfo",userLogInfo);
            // contoh kalau mau hapus setelah dipakai
            // session.removeAttribute("USER_ROLE");
        }
        if (request.getSession(false) != null) {
            String sessionId = request.getSession().getId();

            // Update status di DB
            sessionLogRepo.findBySessionIdAndStatus(sessionId, "ACTIVE").ifPresent(log -> {
                log.setStatus("LOGOUT");
                log.setLogoutTime(LocalDateTime.now());
                sessionLogRepo.save(log);
                System.out.println("Session log updated to LOGOUT");
            });
            /*
            Cuekin duklku aja ini untuk customer login
             */
            AuthenticatedCustomerModel logoutCust = (AuthenticatedCustomerModel) request.getSession().getAttribute("loggedInCustomer");
            if(logoutCust!=null) {
                request.setAttribute("logoutCust",logoutCust);
                System.out.println("logoutCust.getClientId()="+logoutCust.getClientId());
            }

            // Invalidate session
            request.getSession().invalidate();
        }

        // Bersihkan security context
        SecurityContextHolder.clearContext();
    }
        // Redirect ke URL custom


}
