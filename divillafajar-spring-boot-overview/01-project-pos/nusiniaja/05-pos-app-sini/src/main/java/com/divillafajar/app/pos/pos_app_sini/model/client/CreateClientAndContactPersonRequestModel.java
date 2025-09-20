package com.divillafajar.app.pos.pos_app_sini.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientAndContactPersonRequestModel {
    private String coName;
    private String type;
    private String street;
    private String additional;
    private String city;
    private String prov;
    private String country;
    private String zip;
    private String coEmail;
    private String coPhone;
    private String picFirstName;
    private String picLastName;
    private String picPosition;
    private String picPhone;
    private String email;
}
