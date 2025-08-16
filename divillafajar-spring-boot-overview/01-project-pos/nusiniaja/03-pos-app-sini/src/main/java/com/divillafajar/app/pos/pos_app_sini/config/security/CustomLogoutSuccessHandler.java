package com.divillafajar.app.pos.pos_app_sini.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${app.logout.redirect-url:/nowhere}")
    private String redirectUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        String redirectUrl = "/login"; // default redirect
        //System.out.println("lonLogoutSuccess ="+authentication.getAuthorities());
        if (authentication != null) {
            // Redirect berdasarkan role
            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/login";
            }
            else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                redirectUrl = "customer/login";

            }
        }
        else {
            /*
            ** > max iddle time / expired
             */
            redirectUrl = "/session-expired";
        }
        System.out.println("logout redirectUrl ="+redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
