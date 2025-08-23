package com.divillafajar.app.pos.pos_app_sini.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequestModel {

    private String clientName;
    private String email;
    private String address;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String status;
}
