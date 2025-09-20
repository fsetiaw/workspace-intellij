package com.divillafajar.app.pos.pos_app_sini.io.entity.employee.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import jakarta.persistence.*;

public class EmploymentDTO {
    private String pubId;
    private Long id;
    private EmployeeEntity employee;
    private ClientEntity client;
}
