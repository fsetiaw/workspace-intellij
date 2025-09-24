package com.divillafajar.app.pos.pos_app_sini.io.entity.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.address.AddressEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.employee.EmployeeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="UserEntity")
@Table(name="user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phone", "email"})
})
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1169334999915236294L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pub_id", updatable = false, nullable = false)
    private String pubId;

    @PrePersist
    public void generateId() {
        if (pubId == null) {
            pubId = UUID.randomUUID().toString(); // UUID jadi string
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NamePassEntity> auths = new ArrayList<>();

    @Column(name = "email_verification_token", nullable = true)
    private String emailVerificationToken;

    @Column(name = "email_verification_status", nullable = true)
    private boolean emailVerificationStatus = false;

    @Column(name = "email_verified_timestamp", nullable = true)
    private LocalDateTime email_verified_timestamp;

    @Column(name = "registration_timestamp", nullable = true)
    private LocalDateTime registration_timestamp;

    @Column(name = "registration_expiry_timestamp", nullable = true)
    private LocalDateTime registration_expiry_timestamp;

    /*
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NamePassEntity userAuthDetails;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CustomerEntity customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private EmployeeEntity employee;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;
    */

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = true)
    private String lastName;

    @Column(name = "email", length = 125, nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 25, nullable = false)
    private String phone;

    //KTP
    @Column(name = "nik", length = 55, nullable = true)
    private String nik;

}
