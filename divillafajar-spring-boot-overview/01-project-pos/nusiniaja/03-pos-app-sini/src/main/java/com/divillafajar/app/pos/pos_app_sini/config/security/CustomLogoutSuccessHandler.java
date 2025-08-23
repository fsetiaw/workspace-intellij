package com.divillafajar.app.pos.pos_app_sini.config.security;

import com.divillafajar.app.pos.pos_app_sini.model.customer.AuthenticatedCustomerModel;
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
        AuthenticatedCustomerModel logoutCust = (AuthenticatedCustomerModel) request.getAttribute("logoutCust");
        request.removeAttribute("logoutCust");
        //Long clientId = (Long)request.getAttribute("clientId");
        if(logoutCust==null) {
            System.out.println("onLogoutSuccess logoutCust is null=");
            /*
            ** Mencet logout setelah > maxIdleTime
             */
            response.sendRedirect(request.getContextPath() + "/session-expired");
            return;
            //redirectUrl = "/session-expired";
        }
        else {
            System.out.println("lonLogoutSuccess client id ="+logoutCust.getClientId());
            if (authentication != null) {
                System.out.println("authentication not null");
                // Redirect berdasarkan role
                if (authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    redirectUrl = "/login";
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


        System.out.println("logout redirectUrl ="+redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
