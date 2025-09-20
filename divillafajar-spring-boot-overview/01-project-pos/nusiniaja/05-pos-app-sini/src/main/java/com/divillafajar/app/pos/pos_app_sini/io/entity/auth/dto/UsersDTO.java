package com.divillafajar.app.pos.pos_app_sini.io.entity.auth.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.AuthorityEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.GuestEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private Long id;
    private String username;
    private String password;
    private boolean enabled = true;
    private List<AuthorityEntity> authorities = new ArrayList<>();
    private GuestEntity guest;
    private EmploymentEntity employment;
}
