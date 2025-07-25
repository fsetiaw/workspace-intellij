package com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
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
    private String addressName;
    private String recipientName;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String username;
    private String password;
    private String authority;

}
