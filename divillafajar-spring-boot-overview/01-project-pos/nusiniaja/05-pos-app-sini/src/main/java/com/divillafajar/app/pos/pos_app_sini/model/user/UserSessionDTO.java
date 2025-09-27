package com.divillafajar.app.pos.pos_app_sini.model.user;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class UserSessionDTO {
    private String username;
    private String userPid;
    private String clientPid;
    private String fullName;
    private String role;
    private String sessionId;
    private String clientName;
    private String clientListAliasName;
    private String clientType;
    private String userAgent;
    private LocalDateTime loginTime;

}
