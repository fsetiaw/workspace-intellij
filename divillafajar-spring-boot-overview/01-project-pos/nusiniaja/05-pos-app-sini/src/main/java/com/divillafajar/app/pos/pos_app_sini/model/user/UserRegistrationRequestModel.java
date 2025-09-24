package com.divillafajar.app.pos.pos_app_sini.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequestModel {
    //private UserDetailsRequestModel userDetailsRequestModel;
    //private UserAddressDetailsRequestModel userAddressDetailsRequestModel;
    private String firstName;
    private String lastName;
    private String businessName;
    private String clientBusinessField;
    private String phone;
    private String email;
    private String pwd;
    private String username;
    private String superKey;
    /*
    **  JGN DIPAKE NANTI ORANG REGISTYER SEBAGAI ADMIN
    **
    private String authority;
     */
}
