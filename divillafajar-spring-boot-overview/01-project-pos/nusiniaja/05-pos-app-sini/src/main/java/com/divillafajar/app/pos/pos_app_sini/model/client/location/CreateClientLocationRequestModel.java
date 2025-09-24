package com.divillafajar.app.pos.pos_app_sini.model.client.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientLocationRequestModel {
    private String addressName;
    private String addressNickname;
    private String locationCategory;
    private String guestCapacity;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String phone;
    private String email;
}
