package com.divillafajar.app.pos.pos_app_sini.model.client;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequestModel  {
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String clientType;
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
