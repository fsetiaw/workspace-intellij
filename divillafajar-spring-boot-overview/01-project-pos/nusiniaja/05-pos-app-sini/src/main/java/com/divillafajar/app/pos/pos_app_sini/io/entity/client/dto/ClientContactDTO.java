package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientContactDTO {
    private Long id;
    private String pubId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String contactPosition;
    private Boolean deleted;
    private String username;
    private String pwd;
}
