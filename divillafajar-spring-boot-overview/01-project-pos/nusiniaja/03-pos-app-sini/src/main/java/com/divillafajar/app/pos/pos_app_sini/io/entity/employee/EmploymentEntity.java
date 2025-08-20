package com.divillafajar.app.pos.pos_app_sini.io.entity.employee;

import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EmploymentEntity")
@Table(name = "employment")
public class EmploymentEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6246372901946257742L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}
