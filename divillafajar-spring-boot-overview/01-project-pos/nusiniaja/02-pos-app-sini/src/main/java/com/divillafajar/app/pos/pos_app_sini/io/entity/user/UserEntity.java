package com.divillafajar.app.pos.pos_app_sini.io.entity.user;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
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
    private int id;

    @Column(name = "pub_id", length = 30, nullable = false)
    private String pubId;

    @Column(name = "email_verification_token", nullable = true)
    private String emailVerificationToken;

    @Column(name = "email_verification_status", nullable = false)
    private boolean emailVerificationStatus = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NamePassEntity userAuthDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = true)
    private String lastName;

    @Column(name = "email", length = 125, nullable = false)
    private String email;

    @Column(name = "phone", length = 25, nullable = false)
    private String phone;

}
