package com.divillafajar.app.pos.pos_app_sini.ws.model.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginRequestModel {
    private String phoneNumber;
    private String aliasName;
}
