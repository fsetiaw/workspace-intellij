package com.divillafajar.app.pos.pos_app_sini.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/home"; // default

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_CUSTOMER")) {
                redirectUrl = "/guest-home";
                break;
            }
            /*else if (role.equals("ROLE_EMPLOYEE")) {
                redirectUrl = "/employee/home";
                break;
            } else if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/home";
                break;
            }

             */
        }

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
