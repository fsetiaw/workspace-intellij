package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLog;
import com.divillafajar.app.pos.pos_app_sini.model.customer.AuthenticatedCustomerModel;
import com.divillafajar.app.pos.pos_app_sini.model.user.UserSessionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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


        System.out.println("onLogoutSuccess CALLED");
        String redirectUrl = "/login"; // default redirect
        AuthenticatedCustomerModel logoutCust = (AuthenticatedCustomerModel) request.getAttribute("logoutCust");
        UserSessionDTO userLog = (UserSessionDTO) request.getAttribute("userLogInfo");
        //String logout_role = (String) session.getAttribute("USER_ROLE");
        //System.out.println("logout_role=="+logout_role);
        //request.removeAttribute("logoutCust");
        //Long clientId = (Long)request.getAttribute("clientId");
        /*
        if(userLog.getEmployeeClientId()!=null) {
            System.out.println("userLog.getEmployeeClientId()=>"+userLog.getEmployeeClientId());
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        else if(logoutCust==null) {
            System.out.println("onLogoutSuccess logoutCust is null=");
            /*
            ** Mencet logout setelah > maxIdleTime

            response.sendRedirect(request.getContextPath() + "/session-expired");
            return;
            //redirectUrl = "/session-expired";
        }
        */
        if(false) {}
        else {
            //System.out.println("lonLogoutSuccess client id ="+logoutCust.getClientId());
            if (authentication != null) {
                System.out.println("authentication not null= "+ authentication.getAuthorities());
                // Redirect berdasarkan role
                if (authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    redirectUrl = request.getContextPath() +"/login";
                }
                else if (authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                    redirectUrl = "customer/login?clientId="+logoutCust.getClientId();// + customerDTO.getClientId())";

                }
            }
            else {
                System.out.println("authentication is null");
                /*
                 ** > max iddle time / expired
                 */
                redirectUrl = "/session-expired";
            }
        }

        // 🔹 Hapus session / attribute yang masih tersisa
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();   // ini hapus semua session attribute
        }



        System.out.println("logout redirectUrl ="+redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
