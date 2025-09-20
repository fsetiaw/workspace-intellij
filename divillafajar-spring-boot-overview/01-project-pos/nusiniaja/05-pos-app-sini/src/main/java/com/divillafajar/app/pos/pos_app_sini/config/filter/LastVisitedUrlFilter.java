package com.divillafajar.app.pos.pos_app_sini.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LastVisitedUrlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String fullPath = uri + (query != null ? "?" + query : "");

        if (!uri.startsWith("/static") && !uri.startsWith("/auth") && !uri.contains("login")) {
            Cookie cookie = new Cookie("LAST_VISITED_URL", fullPath);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30); // 30 menit
            res.addCookie(cookie);
        }

        chain.doFilter(request, response);
    }
}