package com.divillafajar.app.pos.pos_app_sini.io.entity.session;

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
    @Column(updatable = false)
    //private Long clientId;
    private String username;
    private String role;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private String status;
    private Long clientId;
    private String tableId;

    @Column(updatable = false)
    private LocalDateTime loginTime = LocalDateTime.now();
    private LocalDateTime lastAccessTime;
    private LocalDateTime logoutTime;
    // Getters & Setters
}
