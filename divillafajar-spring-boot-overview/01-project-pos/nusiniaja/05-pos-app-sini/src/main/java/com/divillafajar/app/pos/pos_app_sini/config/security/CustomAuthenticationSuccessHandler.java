package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLogDTO;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserLogedInModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserSessionDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.AuthRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.auth.NamePasRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.session.UserSessionLogRepository;
import com.divillafajar.app.pos.pos_app_sini.model.customer.AuthenticatedCustomerModel;
import com.divillafajar.app.pos.pos_app_sini.model.customer.CustomerLoginRequestModel;
import com.divillafajar.app.pos.pos_app_sini.repo.user.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.service.log.UserSessionLogService;
import com.divillafajar.app.pos.pos_app_sini.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //@Autowired
    private final UserSessionLogService userSessionLogService;
    private final UserSessionLogRepository sessionLogRepo;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final LocaleResolver localeResolver;
/*
    public CustomAuthenticationSuccessHandler(UserSessionLogRepository sessionLogRepo,
              UserSessionLogService userSessionLogService, LocaleResolver localeResolver) {
        this.userSessionLogService=userSessionLogService;
        this.sessionLogRepo=sessionLogRepo;
        this.localeResolver=localeResolver;
    }

 */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("CustomAuthenticationSuccessHandler START");
        String lang = request.getParameter("lang"); // ambil dari form login
        if(lang != null) {
            localeResolver.setLocale(request, response, new Locale(lang));
        }
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
        //session.setAttribute("USER_ROLE", role);




        /*
         **  cek apa ada session dari ip ini yg masih active,
         * bila ditemukan set status "LOGOUT-FORCED"
         */
        String userIp = request.getRemoteAddr();
        List<UserSessionLog> listStillActiveLogFromUserIp = sessionLogRepo.findByIpAddressAndStatusAndLogoutTimeIsNull(userIp,"ACTIVE");
        if(listStillActiveLogFromUserIp!=null && listStillActiveLogFromUserIp.size()>0) {
            listStillActiveLogFromUserIp.forEach(log -> {
                log.setStatus("LOGOUT-FORCED");
                log.setLogoutTime(LocalDateTime.now());
                //sessionLogRepo.save(log);
            });
            sessionLogRepo.saveAll(listStillActiveLogFromUserIp);
        }


        /*
        **  Simpan riwayat session login
         */
        String userAgent = request.getHeader("User-Agent");
        String username = authentication.getName();
        String sessionId = session.getId();

        UserSessionLogDTO prepDTO = userSessionLogService.prepUserSessionLog(username);

        UserSessionLog log = new UserSessionLog();
        BeanUtils.copyProperties(prepDTO,log,"id");
        log.setUsername(username);
        log.setSessionId(sessionId);
        log.setIpAddress(userIp);
        log.setUserAgent(userIp);
        log.setStatus("ACTIVE");
        log.setRole(role);
        log.setUserPid(prepDTO.getUserPid());
        //log.setClientId(clientId);
        log.setTableId(table);

        UserSessionLog userLog =  new UserSessionLog();
        try {
            userLog =  sessionLogRepo.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //UserSessionLog userLog =  sessionLogRepo.save(log);

        UserSessionDTO userSession = new UserSessionDTO();
        BeanUtils.copyProperties(userLog, userSession);
        session.setAttribute("userLogInfo",userSession);

        /*
        UserSessionHistory history = new UserSessionHistory();
        history.setUsername(authentication.getName());
        history.setSessionId(request.getSession().getId());
        history.setLoginTime(LocalDateTime.now());
        history.setIpAddress(request.getRemoteAddr());
        history.setUserAgent(request.getHeader("User-Agent"));
        history.setActive(true);
        userSessionHistoryRepo.save(history);

         */



        //request.getSession().setAttribute("customer", customerDTO);
        String redirectUrl = "/home"; // default

        for (GrantedAuthority authority : authorities) {
            if (role.equals("ROLE_CUSTOMER")) {
                session.setMaxInactiveInterval(0); // never expired -> must logout
                redirectUrl = "/customer/home";
                CustomerLoginRequestModel theCustomer =
                        (CustomerLoginRequestModel) request.getAttribute("theCustomer");

                /*
                sementara biarin aja ini dipakai untuk customer login
                 */
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
                session.setMaxInactiveInterval(-1); //
                redirectUrl = "/v1/superuser/home";
                break;
            }
            else if (role.equals("ROLE_ADMIN")) {


                session.setMaxInactiveInterval(-1);
                redirectUrl = "/v2/admin/home";
                //redirectUrl = "/v2/admin/client/home?pid="+username;
                break;
            }


        }
        redirectStrategy.sendRedirect(request, response, redirectUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
