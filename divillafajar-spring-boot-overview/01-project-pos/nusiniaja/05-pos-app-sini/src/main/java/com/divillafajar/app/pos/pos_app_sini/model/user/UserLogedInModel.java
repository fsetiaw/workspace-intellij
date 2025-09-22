package com.divillafajar.app.pos.pos_app_sini.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogedInModel {
    //private UserDetailsRequestModel userDetailsRequestModel;
    //private UserAddressDetailsRequestModel userAddressDetailsRequestModel;
    private String fullName;
    private String email;
    private String phone;
    private String username;
    private String role;
    /*
    **  JGN DIPAKE NANTI ORANG REGISTYER SEBAGAI ADMIN
    **
    private String authority;
     */
}
