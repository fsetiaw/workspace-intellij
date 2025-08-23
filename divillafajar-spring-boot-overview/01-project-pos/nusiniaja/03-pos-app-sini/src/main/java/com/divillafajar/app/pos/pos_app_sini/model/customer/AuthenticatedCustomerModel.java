package com.divillafajar.app.pos.pos_app_sini.model.customer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedCustomerModel implements Serializable {

    @Serial
    private static final long serialVersionUID = -6253292793547674789L;
    private String phone;
    private String table;
    private Long clientId;
    private String name;


}
