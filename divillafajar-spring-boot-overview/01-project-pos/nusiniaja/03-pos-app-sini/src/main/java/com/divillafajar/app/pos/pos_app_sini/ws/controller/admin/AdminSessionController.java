package com.divillafajar.app.pos.pos_app_sini.ws.controller.admin;

import com.divillafajar.app.pos.pos_app_sini.repo.UserSessionHistoryRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/session")
public class AdminSessionController {
    private final JdbcTemplate jdbcTemplate;
    private final UserSessionHistoryRepo userSessionHistoryRepo;

    public AdminSessionController(JdbcTemplate jdbcTemplate, UserSessionHistoryRepo userSessionHistoryRepo) {
        this.jdbcTemplate=jdbcTemplate;
        this.userSessionHistoryRepo=userSessionHistoryRepo;
    }

    @GetMapping("/active-users")
    public List<Map<String, Object>> getActiveUsers() {
        return jdbcTemplate.queryForList(
                "SELECT SESSION_ID, PRINCIPAL_NAME, LAST_ACCESS_TIME " +
                        "FROM SPRING_SESSION WHERE EXPIRY_TIME > ?",
                System.currentTimeMillis()
        );
    }

    @PostMapping("/logout-user/{sessionId}")
    public ResponseEntity<String> logoutUser(@PathVariable String sessionId) {
        jdbcTemplate.update("DELETE FROM SPRING_SESSION WHERE SESSION_ID = ?", sessionId);

        userSessionHistoryRepo.findAll().stream()
                .filter(h -> h.getSessionId().equals(sessionId) && h.isActive())
                .forEach(h -> {
                    h.setLogoutTime(LocalDateTime.now());
                    h.setActive(false);
                    userSessionHistoryRepo.save(h);
                });

        return ResponseEntity.ok("User dengan session " + sessionId + " telah di-logout.");
    }
}
