package com.divillafajar.app.pos.pos_app_sini.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestModel {
    //private UserDetailsRequestModel userDetailsRequestModel;
    //private UserAddressDetailsRequestModel userAddressDetailsRequestModel;
    private String firstName;
    private String lastName;
    private String email;
    private String addressName;
    private String recipientName;
    private String phone;
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
    /*
    **  JGN DIPAKE NANTI ORANG REGISTYER SEBAGAI ADMIN
    **
    private String authority;
     */
}
