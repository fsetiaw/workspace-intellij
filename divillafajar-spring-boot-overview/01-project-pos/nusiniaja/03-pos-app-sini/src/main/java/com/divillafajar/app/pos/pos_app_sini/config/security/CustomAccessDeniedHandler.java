package com.divillafajar.app.pos.pos_app_sini.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // Contoh redirect ke halaman akses ditolak
        System.out.println("handle denided = "+request.getContextPath());
        response.sendRedirect(request.getContextPath() + "/access-denied");

        // Atau kalau mau langsung kirim status
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }
}
