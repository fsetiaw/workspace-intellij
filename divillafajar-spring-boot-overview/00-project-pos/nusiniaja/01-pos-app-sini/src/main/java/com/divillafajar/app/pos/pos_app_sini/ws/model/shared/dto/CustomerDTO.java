package com.divillafajar.app.pos.pos_app_sini.ws.model.shared.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7032381008394561756L;
    private long id;
    private String phoneNumber;
    private String aliasName;
    private CustomerDetailsEntity customerDetails;
    List<ClientEntity> clients;
}
