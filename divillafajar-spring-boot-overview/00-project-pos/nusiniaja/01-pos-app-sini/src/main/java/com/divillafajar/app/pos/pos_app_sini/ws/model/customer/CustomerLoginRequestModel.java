package com.divillafajar.app.pos.pos_app_sini.ws.model.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CustomerLoginRequestModel {

    @NotBlank(message = "is required")
    @Size(min = 1, message = "is required")
    private String phoneNumber;

    @NotBlank(message = "is required")
    @Size(min = 1, message = "is required")
    private String aliasName;


}
