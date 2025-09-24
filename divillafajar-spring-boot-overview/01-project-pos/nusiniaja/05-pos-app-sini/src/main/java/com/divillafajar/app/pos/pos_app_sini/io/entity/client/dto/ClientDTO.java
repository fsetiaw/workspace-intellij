package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientAddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String pubId;
    private String businessName; //field form
    private String clientName; //copyke sini
    private String clientType;
    //DIBAWAH INI BELUm KEPAKE
    private String clientListAliasName;
    private String clientEmail;
    private String clientPhone;

    private String status;
    private List<ClientAddressEntity> clientAddresses;
    private Set<EmploymentEntity> employments;
    private Boolean deleted;
}
