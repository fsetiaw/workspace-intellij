package com.divillafajar.app.pos.pos_app_sini.io.entity.customer.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO  {

    private Long id;
    private String pubId;
    private long clientId;
    private String username;
    private String table;
    private String phoneNumber;
    private String aliasName;
    private CustomerDetailsEntity customerDetails;
    List<ClientEntity> clients;
	//=======meta data ==============
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}
