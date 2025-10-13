package com.divillafajar.app.pos.pos_app_sini.io.entity.employee.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class EmploymentDTO {
    private String pubId;
    private Long id;
    private EmployeeEntity employee;
    private ClientEntity client;
	//=======meta data ==============
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private Boolean deleted;
}
