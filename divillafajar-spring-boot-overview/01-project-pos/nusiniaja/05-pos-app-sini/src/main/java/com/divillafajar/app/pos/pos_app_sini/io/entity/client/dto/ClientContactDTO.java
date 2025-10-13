package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String username;
    private String pwd;
	//=======meta data ==============
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}
