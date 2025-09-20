package com.divillafajar.app.pos.pos_app_sini.io.entity.employee.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.model.ContactPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private String pubId;
    private List<ContactPerson> contacts;
    private Set<EmploymentEntity> employments;
    private UserEntity user;;
    private NamePassEntity userAuthDetails;
}
