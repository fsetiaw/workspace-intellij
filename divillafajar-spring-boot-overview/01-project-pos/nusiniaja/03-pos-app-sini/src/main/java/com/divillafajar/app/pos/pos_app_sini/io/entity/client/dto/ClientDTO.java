package com.divillafajar.app.pos.pos_app_sini.io.entity.client.dto;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientDetailsEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmploymentEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String pubId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String status;
    private ClientDetailsEntity clientDetails;
    private Set<EmploymentEntity> employments;
}
