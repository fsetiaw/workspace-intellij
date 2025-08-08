package com.divillafajar.app.pos.pos_app_sini.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse(null);
        session.setAttribute("USER_ROLE", role);
        //request.getSession().setAttribute("customer", customerDTO);
        String redirectUrl = "/home"; // default
        System.out.println("CustomAuthenticationSuccessHandler START");
        System.out.println("Get Principle = "+authentication.getPrincipal());
        for (GrantedAuthority authority : authorities) {
            if (role.equals("ROLE_CUSTOMER")) {
                session.setMaxInactiveInterval(60 * 5); // 5 menit
                System.out.println("USER ROLE = CUSTOMER");
                redirectUrl = "/customer/home";
                break;
            }
            else if (role.equals("ROLE_EMPLOYEE")) {
                session.setMaxInactiveInterval(60 * 60); //
                System.out.println("USER ROLE = EMPLOYEE");
                redirectUrl = "/home";
                break;
            }
            /*
            else if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/home";
                break;
            }

             */
        }
        System.out.println("SEND redirectUrl="+redirectUrl);
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
