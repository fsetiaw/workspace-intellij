package com.divillafajar.app.pos.pos_app_sini.model.client.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientLocationRequestModel {
    private String locationCategory;
    private String addressName;
    private String addressNickname;
    private String addressLine1;
    private String addressLine2;
    private String village;
    private String subDistrict;
    private String city;
    private String province;
    private String postalCode;
    private String country;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String contactPosition;
    private String status;
}
