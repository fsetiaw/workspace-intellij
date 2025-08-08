package com.divillafajar.app.pos.pos_app_sini.ws.model.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AuthenticatedCustomerModel {

    private String phone;
    private String table;
    private String name;


}
