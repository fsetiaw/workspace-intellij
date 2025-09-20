package com.divillafajar.app.pos.pos_app_sini.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtils {
    private static boolean isSessionValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false = jangan buat session baru
        if (session == null) {
            return false; // tidak ada session sama sekali
        }

        // Ambil Authentication dari SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // tidak ada auth atau belum login
        }

        // Pastikan bukan anonymous user
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return true; // kalau lolos semua, berarti valid
    }
}
