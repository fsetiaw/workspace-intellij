package com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto.jwt;

import lombok.Data;

@Data
public class JwtLoginRequestDTO {
    private String username;
    private String password;
}
