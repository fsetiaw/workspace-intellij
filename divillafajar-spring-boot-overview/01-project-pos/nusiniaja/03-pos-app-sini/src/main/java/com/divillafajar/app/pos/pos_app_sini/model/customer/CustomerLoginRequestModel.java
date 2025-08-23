package com.divillafajar.app.pos.pos_app_sini.model.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CustomerLoginRequestModel {

    @NotNull(message = "Phone number is required")
    @Size(min = 8, message = "Phone number is required")
    private String username;
    private String password;
    private String table;
    private Long clientId;
    @NotNull(message = "Name is required")
    @Size(min = 2, message = "Name is required")
    private String aliasName;


}
