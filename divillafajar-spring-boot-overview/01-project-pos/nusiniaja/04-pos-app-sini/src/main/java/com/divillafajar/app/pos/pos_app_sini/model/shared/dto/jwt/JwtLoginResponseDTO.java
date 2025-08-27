package com.divillafajar.app.pos.pos_app_sini.model.shared.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtLoginResponseDTO {
    private String token;
    /*
    public JwtLoginResponseDTO(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
     */
}
