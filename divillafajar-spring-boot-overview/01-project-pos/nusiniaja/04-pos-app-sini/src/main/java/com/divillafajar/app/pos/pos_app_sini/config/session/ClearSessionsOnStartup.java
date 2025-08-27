package com.divillafajar.app.pos.pos_app_sini.config.session;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

//@Component
public class ClearSessionsOnStartup {
    private final JdbcTemplate jdbcTemplate;

    public ClearSessionsOnStartup(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void clearSessions() {
        try {
            jdbcTemplate.update("DELETE FROM SPRING_SESSION_ATTRIBUTES");
            jdbcTemplate.update("DELETE FROM SPRING_SESSION");
            System.out.println("All sessions cleared on startup.");
        }
        catch (Exception e) {}

    }
}
