package com.divillafajar.app.pos.pos_app_sini.ws.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4569776359027847181L;
    private long id;
    private String publicUserId;
    private String firstName;
    private String lastName;
    private String email;

}
