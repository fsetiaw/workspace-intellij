package com.divillafajar.app.pos.pos_app_sini.io.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_session_log")
public class UserSessionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private String status;

    @Column(updatable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    private LocalDateTime logoutTime;
    // Getters & Setters
}
