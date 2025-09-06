package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAddressDTO {
    private String id;
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
}
