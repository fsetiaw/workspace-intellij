package com.divillafajar.app.pos.pos_app_sini.io.entity.session;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionLogDTO {
    private Long id;
    private String username;
    private String userPid;
    private String fullName;
    private String role;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private String status;
    private Long employeeClientId;
    private Long customerClientId;
    private String tableId;
    private LocalDateTime loginTime = LocalDateTime.now();
    private LocalDateTime lastAccessTime;
    private LocalDateTime logoutTime;
}
