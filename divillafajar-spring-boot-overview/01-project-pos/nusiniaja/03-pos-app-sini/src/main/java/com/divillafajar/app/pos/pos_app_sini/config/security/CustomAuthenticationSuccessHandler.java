package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserSessionHistory;
import com.divillafajar.app.pos.pos_app_sini.repo.UserSessionHistoryRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //@Autowired
    private final UserSessionHistoryRepo userSessionHistoryRepo;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomAuthenticationSuccessHandler(UserSessionHistoryRepo userSessionHistoryRepo) {
        this.userSessionHistoryRepo=userSessionHistoryRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("CustomAuthenticationSuccessHandler START");
        /*
        **  Simpan riwayat session login
         */
        UserSessionHistory history = new UserSessionHistory();
        history.setUsername(authentication.getName());
        history.setSessionId(request.getSession().getId());
        history.setLoginTime(LocalDateTime.now());
        history.setIpAddress(request.getRemoteAddr());
        history.setUserAgent(request.getHeader("User-Agent"));
        history.setActive(true);
        userSessionHistoryRepo.save(history);
        System.out.println("userSessionHistoryRepo SAVED");

        HttpSession session = request.getSession();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse(null);
        session.setAttribute("USER_ROLE", role);
        //request.getSession().setAttribute("customer", customerDTO);
        String redirectUrl = "/home"; // default

        System.out.println("Get Principle = "+authentication.getPrincipal());
        for (GrantedAuthority authority : authorities) {
            if (role.equals("ROLE_CUSTOMER")) {
                session.setMaxInactiveInterval(0); // never expired -> must logout
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
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
