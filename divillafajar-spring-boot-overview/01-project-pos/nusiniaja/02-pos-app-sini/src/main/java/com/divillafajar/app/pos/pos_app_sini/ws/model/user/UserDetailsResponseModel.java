package com.divillafajar.app.pos.pos_app_sini.ws.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseModel {
    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;
}
