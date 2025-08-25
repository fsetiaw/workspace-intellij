package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import com.divillafajar.app.pos.pos_app_sini.model.customer.AuthenticatedCustomerModel;
import com.divillafajar.app.pos.pos_app_sini.model.customer.CustomerLoginRequestModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //@Autowired
    //private final UserSessionHistoryRepo userSessionHistoryRepo;
    private final UserSessionLogRepository sessionLogRepo;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomAuthenticationSuccessHandler(UserSessionLogRepository sessionLogRepo) {
        //this.userSessionHistoryRepo=userSessionHistoryRepo;
        this.sessionLogRepo=sessionLogRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("CustomAuthenticationSuccessHandler START");

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }

        Long clientId = (Long)request.getAttribute("clientId");
        String table = (String)request.getAttribute("table");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse(null);
        session.setAttribute("USER_ROLE", role);
        /*
        **  Simpan riwayat session login
         */

        String username = authentication.getName();
        String sessionId = session.getId();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        UserSessionLog log = new UserSessionLog();
        log.setUsername(username);
        log.setSessionId(sessionId);
        log.setIpAddress(ip);
        log.setUserAgent(userAgent);
        log.setStatus("ACTIVE");
        log.setRole(role);
        log.setClientId(clientId);
        log.setTableId(table);

        sessionLogRepo.save(log);
        /*
        UserSessionHistory history = new UserSessionHistory();
        history.setUsername(authentication.getName());
        history.setSessionId(request.getSession().getId());
        history.setLoginTime(LocalDateTime.now());
        history.setIpAddress(request.getRemoteAddr());
        history.setUserAgent(request.getHeader("User-Agent"));
        history.setActive(true);
        userSessionHistoryRepo.save(history);
        System.out.println("userSessionHistoryRepo SAVED");

         */



        //request.getSession().setAttribute("customer", customerDTO);
        String redirectUrl = "/home"; // default

        System.out.println("Get Principle = "+authentication.getPrincipal());
        for (GrantedAuthority authority : authorities) {
            if (role.equals("ROLE_CUSTOMER")) {
                session.setMaxInactiveInterval(0); // never expired -> must logout
                redirectUrl = "/customer/home";
                CustomerLoginRequestModel theCustomer =
                        (CustomerLoginRequestModel) request.getAttribute("theCustomer");

                AuthenticatedCustomerModel nuCust = new AuthenticatedCustomerModel();
                nuCust.setName(theCustomer.getAliasName());
                nuCust.setPhone(theCustomer.getUsername());
                nuCust.setTable(theCustomer.getTable());
                nuCust.setClientId(theCustomer.getClientId());
                session.setAttribute("loggedInCustomer",nuCust);
                break;
            }
            else if (role.equals("ROLE_EMPLOYEE")) {
                session.setMaxInactiveInterval(60); //
                redirectUrl = "/home";
                break;
            }
            else if (role.equals("ROLE_SUPERADMIN")) {
                session.setMaxInactiveInterval(60); //
                redirectUrl = "/super/home";
                break;
            }


        }
        redirectStrategy.sendRedirect(request, response, redirectUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
